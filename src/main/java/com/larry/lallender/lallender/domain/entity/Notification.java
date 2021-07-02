package com.larry.lallender.lallender.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Notification {
    private final Long scheduleId;
    private final User writer;
    private final String title;
    private final LocalDateTime notifyAt;
}
