package com.larry.lallender.lallender.service;

import com.larry.lallender.lallender.domain.entity.*;
import com.larry.lallender.lallender.domain.repository.EngagementRepository;
import com.larry.lallender.lallender.domain.repository.ScheduleRepository;
import com.larry.lallender.lallender.dto.EventCreateReq;
import com.larry.lallender.lallender.dto.NotificationCreateReq;
import com.larry.lallender.lallender.dto.TaskCreateReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final EngagementRepository engagementRepository;
    private final UserService userService;

    public Task createTask(User writer, TaskCreateReq req) {
        final Schedule schedule = scheduleRepository.save(Schedule.ofTask(req.getTitle(),
                                                                          req.getDescription(),
                                                                          req.getTaskAt(),
                                                                          writer));
        return schedule.toTask();
    }

    public EventWithEngagement createEvent(User writer, EventCreateReq req) {
        final List<Engagement> engagementList =
                engagementRepository.findAllByAttendeeIdInAndSchedule_EndAtBefore(req.getAttendeeIds(),
                                                                                 req.getStartAt());
        if (engagementList
                .stream()
                .anyMatch(e -> e.getEvent()
                                .isOverlapped(req.getStartAt(), req.getEndAt())
                        && e.getStatus() == EngagementStatus.ACCEPTED)) {
            throw new RuntimeException("cannot create event - time overlap");
        }
        final Event newEvent = scheduleRepository.save(Schedule.ofEvent(req.getStartAt(),
                                                                        req.getEndAt(),
                                                                        req.getTitle(),
                                                                        req.getDescription(),
                                                                        writer))
                                                 .toEvent();
        final List<Engagement> engagements = req.getAttendeeIds()
                                                .stream()
                                                .map(a -> engagementRepository.save(
                                                        Engagement.of(newEvent,
                                                                      userService.findById(a)))
                                                )
                                                .collect(toList());
        return new EventWithEngagement(newEvent, engagements);
    }

    public Notification createNotification(User writer, NotificationCreateReq req) {
        return req.getFlattenedTimes()
                  .stream()
                  .map(notifyAt -> Schedule.ofNotification(notifyAt, req.getTitle(), writer))
                  .map(n -> scheduleRepository.save(n))
                  .findFirst()
                  .map(s -> s.toNotification())
                  .orElseThrow(() -> new RuntimeException("no notification"));
    }
}
