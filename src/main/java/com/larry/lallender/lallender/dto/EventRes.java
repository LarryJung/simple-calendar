package com.larry.lallender.lallender.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.larry.lallender.lallender.domain.entity.ScheduleType;
import com.larry.lallender.lallender.util.Period;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventRes implements ScheduleRes{
    private final Long scheduleId;
    private final LocalDateTime startAt;
    private final LocalDateTime endAt;
    private final String title;
    private final String description;
    private final UserRes writer;

    @Override
    public ScheduleType getScheduleType() {
        return ScheduleType.EVENT;
    }

    @JsonIgnore
    public Period getPeriod() {
        return Period.of(startAt, endAt);
    }
}
