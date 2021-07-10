package com.larry.lallender.lallender.service;

import com.larry.lallender.lallender.domain.entity.*;
import com.larry.lallender.lallender.domain.repository.EngagementRepository;
import com.larry.lallender.lallender.domain.repository.ScheduleRepository;
import com.larry.lallender.lallender.dto.*;
import com.larry.lallender.lallender.exception.CalendarException;
import com.larry.lallender.lallender.util.Period;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.larry.lallender.lallender.exception.ErrorCode.EVENT_CREATE_OVERLAPPED_PERIOD;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final EmailService emailService; // 이건 좀 느슨하게...
    private final ScheduleRepository scheduleRepository;
    private final EngagementRepository engagementRepository;
    private final UserService userService;

    @Transactional
    public TaskRes createTask(AuthUser authUser, TaskCreateReq req) {
        final User writer = userService.findById(authUser.getId());
        final Schedule schedule = scheduleRepository.save(Schedule.ofTask(req.getTitle(),
                                                                          req.getDescription(),
                                                                          req.getTaskAt(),
                                                                          writer));
        return schedule.toTask()
                       .toRes();
    }

    @Transactional
    public EventWithEngagement createEvent(AuthUser authUser, EventCreateReq req) {
        final User writer = userService.findById(authUser.getId());
        final List<Engagement> engagementList =
                engagementRepository.findAllByAttendeeIdInAndSchedule_EndAtAfter(req.getAttendeeIds(),
                                                                                 req.getStartAt());
        if (engagementList
                .stream()
                .anyMatch(e -> e.getEvent()
                                .isOverlapped(req.getStartAt(), req.getEndAt())
                        && e.getStatus() == EngagementStatus.ACCEPTED)) {
            throw new CalendarException(EVENT_CREATE_OVERLAPPED_PERIOD);
        }
        final Event event = scheduleRepository.save(Schedule.ofEvent(req.getStartAt(),
                                                                     req.getEndAt(),
                                                                     req.getTitle(),
                                                                     req.getDescription(),
                                                                     writer))
                                              .toEvent();
        final List<User> attendees = req.getAttendeeIds()
                                        .stream()
                                        .map(userService::findById)
                                        .collect(toList());
        final List<EngagementRes> engagements = attendees
                .stream()
                .map(a -> engagementRepository.save(Engagement.of(event, a))
                                              .toRes())
                .peek(e -> emailService.send(
                        new EngagementEmailStuff(e.getId(),
                                                 e.getAttendee()
                                                  .getEmail(),
                                                 attendees.stream()
                                                          .map(User::getEmail)
                                                          .collect(toList()),
                                                 e.getEvent()
                                                  .getTitle(),
                                                 e.getEvent()
                                                  .getPeriod()
                        )
                ))
                .collect(toList());
        return new EventWithEngagement(event.toRes(), engagements);
    }

    @Transactional
    public List<NotificationRes> createNotification(AuthUser authUser, NotificationCreateReq req) {
        final User writer = userService.findById(authUser.getId());
        return req.getFlattenedTimes()
                  .stream()
                  .map(notifyAt -> scheduleRepository.save(Schedule.ofNotification(notifyAt,
                                                                                   req.getTitle(),
                                                                                   writer))
                                                     .toNotification()
                                                     .toRes())
                  .collect(toList());
    }

    @Transactional
    public List<ScheduleRes> getSchedules(AuthUser authUser) {
        return scheduleRepository.findAllByWriter_Id(authUser.getId())
                                 .stream()
                                 .map(Schedule::toRes)
                                 .collect(toList());
    }

    @Transactional
    public List<ScheduleRes> getSchedulesByDay(AuthUser authUser, LocalDate date) {
        return getSchedulesByFilter(authUser,
                                    (schedule) -> schedule.isOverlapped(date),
                                    (engagement -> engagement.isOverlapped(date)));
    }

    @Transactional
    public List<ScheduleRes> getSchedulesByMonth(AuthUser authUser, YearMonth yearMonth) {
        final Period period = Period.of(yearMonth.atDay(1), yearMonth.atEndOfMonth());
        return getSchedulesByFilter(authUser,
                                    (schedule) -> schedule.isOverlapped(period),
                                    (engagement -> engagement.isOverlapped(period)));
    }

    @Transactional
    public List<ScheduleRes> getSchedulesByWeek(AuthUser authUser, LocalDate startOfWeek) {
        final Period period = Period.of(startOfWeek, startOfWeek.plusDays(6));
        return getSchedulesByFilter(authUser,
                                    (schedule) -> schedule.isOverlapped(period),
                                    (engagement -> engagement.isOverlapped(period)));
    }

    private List<ScheduleRes> getSchedulesByFilter(AuthUser authUser,
                                                   Function<Schedule, Boolean> scheduleFilter,
                                                   Function<Engagement, Boolean> engagementFilter
    ) {
        return Stream.concat(
                scheduleRepository.findAllByWriter_Id(authUser.getId())
                                  .stream()
                                  .filter(scheduleFilter::apply)
                                  .map(Schedule::toRes),
                engagementRepository.findAllByAttendeeId(authUser.getId())
                                    .stream()
                                    .filter(engagementFilter::apply)
                                    .map(engagement -> engagement.getEvent()
                                                                 .toRes())
        )
                     .collect(toList());
    }
}
