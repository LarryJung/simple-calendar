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
import java.time.YearMonth;
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
            @Valid @RequestBody EventCreateReq eventCreateReq,
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

    // 조회 Api 에는 null 체크가 들어가야 한다.

    @GetMapping("/day")
    public List<ScheduleRes> getSchedulesByDay(
            AuthUser authUser,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return scheduleService.getSchedulesByDay(authUser, date);
    }

    @GetMapping("/week")
    public List<ScheduleRes> getSchedulesByWeek(
            AuthUser authUser,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startOfWeek
    ) {
        return scheduleService.getSchedulesByWeek(authUser,
                                                  startOfWeek == null ? LocalDate.now() :
                                                          startOfWeek);
    }

    @GetMapping("/month")
    public List<ScheduleRes> getSchedulesByDay(
            AuthUser authUser,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") String yearMonth
    ) {
        return scheduleService.getSchedulesByMonth(authUser,
                                                   yearMonth == null ? YearMonth.now() :
                                                           YearMonth.parse(yearMonth));
    }
}

// TODO docker, mysql, 배치, queryDSL
