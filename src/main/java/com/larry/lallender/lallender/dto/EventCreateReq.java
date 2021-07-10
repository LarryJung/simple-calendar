package com.larry.lallender.lallender.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class EventCreateReq {
    @NotNull
    private final String title;
    private final String description;
    @NotNull
    private final LocalDateTime startAt;
    @NotNull
    private final LocalDateTime endAt;
    private final List<Long> attendeeIds;
}
