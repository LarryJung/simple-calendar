package com.larry.lallender.lallender.dto;

import com.larry.lallender.lallender.util.DayOfWeekType;
import com.larry.lallender.lallender.util.TimeUnit;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Data
public class NotificationCreateReq {
    private final String title;
    private final LocalDateTime notifyAt;
    private final RepeatInfo repeatInfo;

    public RepeatInfo getRepeatInfo() {
        if (repeatInfo == null) {
            return new RepeatInfo(new RepeatPeriod(1, TimeUnit.DAY), 1, null, null);
        }
        return repeatInfo;
    }

    public List<LocalDateTime> getFlattenedTimes() {
        return Collections.emptyList();
    }

    @Data
    public static class RepeatInfo {
        private final RepeatPeriod repeatPeriod;
        private final int repeatTimes;
        private final DayOfWeekType repeatDayOfWeek;
        private final MonthRepeatType monthRepeatType;
    }

    @Data
    static class RepeatPeriod {
        private final int quantity;
        private final TimeUnit timeUnit;
    }

    enum MonthRepeatType {
        BY_DAY_OF_NUMBER, BY_DAY_OF_WEEK
    }
}
