package com.larry.lallender.lallender.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationRes implements ScheduleRes {
    private final Long scheduleId;
    private final UserRes writer;
    private final String title;
    private final LocalDateTime notifyAt;
}
