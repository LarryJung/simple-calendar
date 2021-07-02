package com.larry.lallender.lallender.domain.entity;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Task {
    private Long id;
    private LocalDateTime taskAt;
    private String title;
    private String description;
    private User writer;
    private LocalDateTime createdAt;

    public Task(final Long id,
                final LocalDateTime taskAt,
                final String title,
                final String description,
                final User writer) {
        this.id = id;
        this.taskAt = taskAt;
        this.title = title;
        this.description = description;
        this.writer = writer;
    }
}
