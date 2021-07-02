package com.larry.lallender.lallender;

import java.time.LocalDateTime;

public class Event {
    private Long id;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private LocalDateTime createdAt;
    private String title;
    private String description;
    private User writer;
}
