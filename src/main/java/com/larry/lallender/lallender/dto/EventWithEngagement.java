package com.larry.lallender.lallender.dto;

import lombok.Data;

import java.util.List;

@Data
public class EventWithEngagement {
    private final EventRes event;
    private final List<EngagementRes> engagements;
}
