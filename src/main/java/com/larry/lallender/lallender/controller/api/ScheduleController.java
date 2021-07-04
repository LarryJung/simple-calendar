package com.larry.lallender.lallender.controller.api;

import com.larry.lallender.lallender.domain.entity.EngagementStatus;
import com.larry.lallender.lallender.dto.*;
import com.larry.lallender.lallender.service.EngagementService;
import com.larry.lallender.lallender.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequestMapping("/api/schedules")
@RestController
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final EngagementService engagementService;

    @PostMapping("/tasks")
    public TaskRes createTask(@Valid @RequestBody TaskCreateReq taskCreateReq,
                              AuthUser authUser) {
        return scheduleService.createTask(authUser, taskCreateReq);
    }

    @PostMapping("/notifications")
    public List<NotificationRes> createTask(
            @Valid @RequestBody NotificationCreateReq notificationCreateReq,
            AuthUser authUser) {
        return scheduleService.createNotification(authUser, notificationCreateReq);
    }

    @PostMapping("/events")
    public EventWithEngagement createEvent(
            @RequestBody EventCreateReq eventCreateReq,
            AuthUser authUser) {
        return scheduleService.createEvent(authUser, eventCreateReq);
    }

    @GetMapping("/events/engagements/{engagementId}")
    public EngagementStatus updateEngagement(
            @PathVariable Long engagementId,
            @RequestParam EngagementReplyType type,
            AuthUser authUser) {
        return engagementService.update(authUser, engagementId, type);
    }

    @GetMapping
    public List<ScheduleRes> getSchedules(
            AuthUser authUser) {
        return scheduleService.getSchedules(authUser);
    }

    @GetMapping("/day")
    public List<ScheduleRes> getSchedulesByDay(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            AuthUser authUser) {
        if (date == null) {
            return scheduleService.getSchedulesByDay(authUser,
                                                     LocalDateTime.now()
                                                                  .toLocalDate());
        }
        return scheduleService.getSchedulesByDay(authUser, date);
    }

    // update, delete ...

}
