package com.larry.lallender.lallender.controller.api;

import com.larry.lallender.lallender.domain.entity.RequestStatus;
import com.larry.lallender.lallender.dto.*;
import com.larry.lallender.lallender.service.EngagementService;
import com.larry.lallender.lallender.service.ScheduleService;
import com.larry.lallender.lallender.service.ShareService;
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
    private final ShareService shareService;
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
    public RequestStatus updateEngagement(
            @PathVariable Long engagementId,
            @RequestParam RequestReplyType type,
            AuthUser authUser) {
        return engagementService.update(authUser, engagementId, type);
    }

    @GetMapping("/day")
    public List<SharedScheduleRes> getSchedulesByDay(
            AuthUser authUser,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return scheduleService.getSchedulesByDay(authUser, date);
    }

    @GetMapping("/week")
    public List<SharedScheduleRes> getSchedulesByWeek(
            AuthUser authUser,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startOfWeek
    ) {
        return scheduleService.getSchedulesByWeek(authUser,
                                                  startOfWeek == null ? LocalDate.now() :
                                                          startOfWeek);
    }

    @GetMapping("/month")
    public List<SharedScheduleRes> getSchedulesByDay(
            AuthUser authUser,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") String yearMonth
    ) {
        return scheduleService.getSchedulesByMonth(authUser,
                                                   yearMonth == null ? YearMonth.now() :
                                                           YearMonth.parse(yearMonth));
    }

    @PostMapping("/shares")
    public void shareSchedule(
            AuthUser authUser,
            @Valid @RequestBody ShareCreateReq shareCreateReq
    ) {
        shareService.createShare(authUser.getId(),
                                 shareCreateReq.getToUserId(),
                                 shareCreateReq.getDirection());
    }

    @PostMapping("/shares/{shareId}")
    public void replyToShareRequest(
            @PathVariable Long shareId,
            @RequestParam RequestReplyType type,
            AuthUser authUser
    ) {
        shareService.replyToShareRequest(shareId, authUser.getId(), type);
    }
}
