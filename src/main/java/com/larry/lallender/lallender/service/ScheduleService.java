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

import static java.util.stream.Collectors.toSet;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final EngagementRepository engagementRepository;

    public Task createTask(User writer,
                           TaskCreateReq req) {
        Schedule schedule = scheduleRepository.save(Schedule.ofTask(req.getTitle(),
                                                                    req.getDescription(),
                                                                    req.getTaskAt(),
                                                                    writer));
        return schedule.toTask();
    }

    public Event createEvent(User writer, EventCreateReq req) {
        List<Engagement> engagementList =
                engagementRepository.findAllByAttendeeIdIn(req.getAttendeeIds());
        if (engagementList
                .stream()
                .noneMatch(e -> e.getEvent()
                                 .isOverlapped(req.getStartAt(), req.getEndAt())
                        && e.getStatus() == EngagementStatus.ACCEPTED)) {
            Event newEvent = scheduleRepository.save(Schedule.ofEvent(req.getStartAt(),
                                                                      req.getEndAt(),
                                                                      req.getTitle(),
                                                                      req.getDescription(),
                                                                      writer))
                                               .toEvent();
            engagementList.stream()
                          .map(Engagement::getAttendee)
                          .collect(toSet())
                          .stream()
                          .map(a -> Engagement.of(newEvent, writer))
                          .forEach(engagementRepository::save);
            return newEvent;
        } else {
            throw new RuntimeException("cannot create event");
        }
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
