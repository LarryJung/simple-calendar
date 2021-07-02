package com.larry.lallender.lallender.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Task {
    private final Long scheduleId;
    private final User writer;
    private final String title;
    private final String description;
    private final LocalDateTime taskAt;
    private final LocalDateTime createdAt;
}
