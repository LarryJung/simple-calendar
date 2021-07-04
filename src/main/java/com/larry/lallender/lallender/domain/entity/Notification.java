package com.larry.lallender.lallender.domain.entity;

import com.larry.lallender.lallender.dto.NotificationRes;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
public class Notification {
    @Getter(value = AccessLevel.PACKAGE)
    private final Schedule schedule;
    private final User writer;
    private final String title;
    private final LocalDateTime notifyAt;

    public NotificationRes toRes() {
        return new NotificationRes(schedule.getId(), writer.toRes(), title, notifyAt);
    }
}
