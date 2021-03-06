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
import java.util.Set;
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
    private final ShareService shareService; // 점점 많아지니 조회서비스로 분해해도 좋을듯

    @Transactional
    public TaskRes createTask(AuthUser authUser, TaskCreateReq req) {
        final User writer = userService.findById(authUser.getId());
        final Schedule schedule = scheduleRepository.save(Schedule.task(req.getTitle(),
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
                        && e.getStatus() == RequestStatus.ACCEPTED)) {
            throw new CalendarException(EVENT_CREATE_OVERLAPPED_PERIOD);
        }
        final Event event = scheduleRepository.save(Schedule.event(req.getStartAt(),
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
                  .map(notifyAt -> scheduleRepository.save(Schedule.notification(notifyAt,
                                                                                 req.getTitle(),
                                                                                 writer))
                                                     .toNotification()
                                                     .toRes())
                  .collect(toList());
    }

    @Transactional
    public List<SharedScheduleRes> getSchedulesByDay(AuthUser authUser, LocalDate date) {
        return getSchedulesByFilter(authUser,
                                    (schedule) -> schedule.isOverlapped(date),
                                    (engagement) -> engagement.isOverlapped(date));
    }

    @Transactional
    public List<SharedScheduleRes> getSchedulesByMonth(AuthUser authUser,
                                                       YearMonth yearMonth) {
        final Period period = Period.of(yearMonth.atDay(1), yearMonth.atEndOfMonth());
        return getSchedulesByFilter(authUser,
                                    (schedule) -> schedule.isOverlapped(period),
                                    (engagement) -> engagement.isOverlapped(period));
    }

    @Transactional
    public List<SharedScheduleRes> getSchedulesByWeek(AuthUser authUser,
                                                      LocalDate startOfWeek) {
        final Period period = Period.of(startOfWeek, startOfWeek.plusDays(6));
        return getSchedulesByFilter(authUser,
                                    (schedule) -> schedule.isOverlapped(period),
                                    (engagement) -> engagement.isOverlapped(period));
    }

    private List<SharedScheduleRes> getSchedulesByFilter(AuthUser authUser,
                                                         Function<Schedule, Boolean> scheduleFilter,
                                                         Function<Engagement, Boolean> engagementFilter
    ) {
        final Set<Long> sharedUserIds = shareService.findSharedUserIdsByUser(authUser);
        return Stream.concat(sharedUserIds.stream(), Stream.of(authUser.getId()))
                     .map(userId -> new SharedScheduleRes(userId,
                                                          userService.findById(userId)
                                                                     .getName(),
                                                          userId.equals(authUser.getId()),
                                                          Stream.concat(
                                                                  scheduleRepository
                                                                          .findAllByWriter_Id(userId)
                                                                          .stream()
                                                                          .filter(scheduleFilter::apply)
                                                                          .map(Schedule::toRes),
                                                                  engagementRepository
                                                                          .findAllByAttendeeId(
                                                                                  userId)
                                                                          .stream()
                                                                          .filter(engagementFilter::apply)
                                                                          .map(engagement -> engagement.getEvent()
                                                                                                       .toRes())
                                                          )
                                                                .collect(toList())))
                     .collect(toList());
    }
}
