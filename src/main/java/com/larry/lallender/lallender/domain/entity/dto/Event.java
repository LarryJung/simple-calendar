package com.larry.lallender.lallender.domain.entity.dto;

import com.larry.lallender.lallender.domain.entity.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Event {
    private final Long scheduleId;
    private final LocalDateTime startAt;
    private final LocalDateTime endAt;
    private final LocalDateTime createdAt;
    private final String title;
    private final String description;
    private final User writer;

    public boolean isOverlapped(LocalDateTime startAt, LocalDateTime endAt) {
        return !(this.endAt.isBefore(startAt) || this.startAt.isAfter(endAt));
    }
}
