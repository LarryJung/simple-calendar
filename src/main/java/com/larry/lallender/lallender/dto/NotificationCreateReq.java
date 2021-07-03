package com.larry.lallender.lallender.dto;

import com.larry.lallender.lallender.util.TimeUnit;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

@Data
public class NotificationCreateReq {
    private final String title;
    private final LocalDateTime notifyAt;
    private final RepeatInfo repeatInfo;

    public RepeatInfo getRepeatInfo() {
        if (repeatInfo == null) {
            return new RepeatInfo(new RepeatPeriod(1, TimeUnit.DAY), 1);
        }
        return repeatInfo;
    }

    public List<LocalDateTime> getFlattenedTimes() {
        if (repeatInfo == null) {
            return Collections.singletonList(notifyAt);
        }
        return IntStream.range(0, repeatInfo.repeatTimes)
                        .mapToObj(i -> {
                                      switch (repeatInfo.repeatPeriod.timeUnit) {
                                          case DAY:
                                              return notifyAt.plusDays((long) repeatInfo.repeatPeriod.unit * i);
                                          case WEEK:
                                              return notifyAt.plusWeeks((long) repeatInfo.repeatPeriod.unit * i);
                                          case MONTH:
                                              return notifyAt.plusMonths((long) repeatInfo.repeatPeriod.unit * i);
                                          case YEAR:
                                              return notifyAt.plusYears((long) repeatInfo.repeatPeriod.unit * i);
                                          default:
                                              throw new RuntimeException("not supported");
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
