package com.larry.lallender.lallender.domain.entity;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Event {
    private Long id;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private LocalDateTime createdAt;
    private String title;
    private String description;
    private User writer;

    public Event(LocalDateTime startAt, LocalDateTime endAt, LocalDateTime createdAt, String title, String description, User writer) {
        this.startAt = startAt;
        this.endAt = endAt;
        this.createdAt = createdAt;
        this.title = title;
        this.description = description;
        this.writer = writer;
    }
}
