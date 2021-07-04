package com.larry.lallender.lallender.service;

import com.larry.lallender.lallender.domain.entity.*;
import com.larry.lallender.lallender.domain.repository.EngagementRepository;
import com.larry.lallender.lallender.domain.repository.ScheduleRepository;
import com.larry.lallender.lallender.dto.*;
import com.larry.lallender.lallender.exception.CalendarException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static com.larry.lallender.lallender.exception.ErrorCode.EVENT_CREATE_OVERLAPPED_PERIOD;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class ScheduleService {
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
        final List<EngagementRes> engagements = req.getAttendeeIds()
                                                   .stream()
                                                   .map(a -> engagementRepository.save(
                                                           Engagement.of(event,
                                                                         userService.findById(a)))
                                                                                 .toRes()
                                                   )
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
        return Stream.concat(engagementRepository.findAllByAttendeeId(authUser.getId())
                                                 .stream()
                                                 .filter(engagement -> engagement.isOverlapped(date))
                                                 .map(engagement -> engagement.getEvent()
                                                                              .toRes()),
                             scheduleRepository.findAllByWriter_Id(authUser.getId())
                                               .stream()
                                               .filter(schedule -> schedule.isOverlapped(date))
                                               .map(Schedule::toRes))
                     .collect(toList());
    }
}
