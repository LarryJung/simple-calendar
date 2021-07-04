package com.larry.lallender.lallender.domain.entity;

import com.larry.lallender.lallender.dto.TaskRes;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
public class Task{
    @Getter(value = AccessLevel.PACKAGE)
    private final Schedule schedule;
    private final User writer;
    private final String title;
    private final String description;
    private final LocalDateTime taskAt;

    public TaskRes toRes() {
        return new TaskRes(schedule.getId(), writer.toRes(), title, description, taskAt);
    }
}
