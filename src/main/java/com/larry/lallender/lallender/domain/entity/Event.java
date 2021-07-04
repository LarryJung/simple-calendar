package com.larry.lallender.lallender.domain.entity;

import com.larry.lallender.lallender.dto.EventRes;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
public class Event {
    @Getter(value = AccessLevel.PACKAGE)
    private final Schedule schedule;
    private final LocalDateTime startAt;
    private final LocalDateTime endAt;
    private final LocalDateTime createdAt;
    private final String title;
    private final String description;
    private final User writer;

    public boolean isOverlapped(LocalDateTime startAt, LocalDateTime endAt) {
        return !(this.endAt.isBefore(startAt) || this.startAt.isAfter(endAt));
    }

    public EventRes toRes() {
        return new EventRes(schedule.getId(), startAt, endAt, title, description, writer.toRes());
    }
}
