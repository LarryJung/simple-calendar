package com.larry.lallender.lallender.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Engagement {
    private Long id;
    private Event event;
    private User attendee;
    private EngagementStatus status;
}
