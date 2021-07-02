package com.larry.lallender.lallender.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class EventCreateReq {
    private final LocalDateTime startAt;
    private final LocalDateTime endAt;
    private final String title;
    private final String description;
    private final List<Long> attendeeIds;
}
