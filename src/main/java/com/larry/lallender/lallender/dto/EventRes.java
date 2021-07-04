package com.larry.lallender.lallender.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventRes implements ScheduleRes{
    private final Long scheduleId;
    private final LocalDateTime startAt;
    private final LocalDateTime endAt;
    private final String title;
    private final String description;
    private final UserRes writer;
}
