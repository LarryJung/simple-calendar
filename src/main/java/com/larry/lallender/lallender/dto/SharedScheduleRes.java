package com.larry.lallender.lallender.dto;

import lombok.Data;

import java.util.List;

@Data
public class SharedScheduleRes {
    private final Long userId;
    private final String name;
    private final Boolean me;
    private final List<ScheduleRes> scheduleResList;
}
