package com.larry.lallender.lallender.util;

import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Period {
    private LocalDateTime startAt;
    private LocalDateTime endAt;

    private Period(LocalDateTime startAt, LocalDateTime endAt) {
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public static Period of(LocalDateTime startAt, @Nullable LocalDateTime endAt) {
        return new Period(startAt, endAt == null ? startAt : endAt);
    }

    public static Period of(LocalDateTime startAt) {
        return new Period(startAt, startAt);
    }

    public boolean isOverlapped(LocalDateTime startAt, LocalDateTime endAt) {
        return !(this.endAt.isBefore(startAt) || this.startAt.isAfter(endAt));
    }

    public boolean isOverlapped(LocalDate date) {
        return !(this.endAt.isBefore(date.atStartOfDay())
                || this.startAt.isAfter(LocalDateTime.of(date, LocalTime.of(23, 59, 59))));
    }
}
