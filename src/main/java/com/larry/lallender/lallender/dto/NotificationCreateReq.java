package com.larry.lallender.lallender.dto;

import com.larry.lallender.lallender.exception.CalendarException;
import com.larry.lallender.lallender.exception.ErrorCode;
import com.larry.lallender.lallender.util.TimeUnit;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

@Data
public class NotificationCreateReq {
    @NotNull
    private final String title;
    @NotNull
    private final LocalDateTime notifyAt;
    private final RepeatInfo repeatInfo;

    public List<LocalDateTime> getFlattenedTimes() {
        if (repeatInfo == null) {
            return Collections.singletonList(notifyAt);
        }
        return IntStream.range(0, repeatInfo.repeatTimes)
                        .mapToObj(i -> {
                                      long increment = (long) repeatInfo.repeatPeriod.unit * i;
                                      switch (repeatInfo.repeatPeriod.timeUnit) {
                                          case DAY:
                                              return notifyAt.plusDays(increment);
                                          case WEEK:
                                              return notifyAt.plusWeeks(increment);
                                          case MONTH:
                                              return notifyAt.plusMonths(increment);
                                          case YEAR:
                                              return notifyAt.plusYears(increment);
                                          default:
                                              throw new CalendarException(ErrorCode.BAD_REQUEST);
                                      }
                                  }
                        )
                        .collect(toList());
    }

    @Data
    public static class RepeatInfo {
        private final RepeatPeriod repeatPeriod;
        private final int repeatTimes;
    }

    @Data
    public static class RepeatPeriod {
        private final int unit;
        private final TimeUnit timeUnit;
    }
}
