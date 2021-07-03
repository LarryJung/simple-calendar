package com.larry.lallender.lallender.dto;

import com.larry.lallender.lallender.util.DayOfWeekType;
import com.larry.lallender.lallender.util.TimeIncrementor;
import com.larry.lallender.lallender.util.TimeUnit;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

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
        if (repeatInfo == null) {
            return Collections.singletonList(notifyAt);
        }
        return IntStream.range(0, repeatInfo.repeatTimes)
                        .mapToObj(i -> notifyAt.plusDays(repeatInfo.repeatPeriod.unit * i))
                        .collect(toList());
    }

    @Data
    public static class RepeatInfo {
        private final RepeatPeriod repeatPeriod;
        private final int repeatTimes;
        private final DayOfWeekType repeatDayOfWeek;
        private final MonthRepeatType monthRepeatType;
    }

    @Data
    public static class RepeatPeriod {
        private final int unit;
        private final TimeUnit timeUnit;
    }

    enum MonthRepeatType {
        BY_DAY_OF_NUMBER, BY_DAY_OF_WEEK
    }
}
