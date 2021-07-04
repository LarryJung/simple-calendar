package com.larry.lallender.lallender.dto;

import com.larry.lallender.lallender.domain.entity.ScheduleType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationRes implements ScheduleRes {
    private final Long scheduleId;
    private final UserRes writer;
    private final String title;
    private final LocalDateTime notifyAt;

    @Override
    public ScheduleType getScheduleType() {
        return ScheduleType.NOTIFICATION;
    }
}
