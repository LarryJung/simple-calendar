package com.larry.lallender.lallender.util;

import java.time.LocalDateTime;

@FunctionalInterface
public interface TimeIncrementor {
    LocalDateTime increment(LocalDateTime time);
}
