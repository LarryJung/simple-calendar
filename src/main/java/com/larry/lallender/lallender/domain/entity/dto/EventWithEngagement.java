package com.larry.lallender.lallender.domain.entity.dto;

import com.larry.lallender.lallender.domain.entity.Engagement;
import lombok.Data;

import java.util.List;

@Data
public class EventWithEngagement {
    private final Event event;
    private final List<Engagement> engagements;
}
