package com.larry.lallender.lallender.domain.entity;

import com.larry.lallender.lallender.domain.entity.dto.Event;
import com.larry.lallender.lallender.domain.entity.dto.Notification;
import com.larry.lallender.lallender.domain.entity.dto.ScheduleType;
import com.larry.lallender.lallender.domain.entity.dto.Task;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder(access = AccessLevel.PRIVATE)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Schedule {
    @Id
    @GeneratedValue
    private Long id;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String title;
    private String description;

    @JoinColumn(name = "writer_id")
    @ManyToOne(optional = false)
    private User writer;

    private ScheduleType scheduleType;
    private LocalDateTime createdAt;

    public static Schedule ofEvent(Event event) {
        return ofEvent(event.getStartAt(),
                       event.getEndAt(),
                       event.getTitle(),
                       event.getDescription(),
                       event.getWriter());
    }

    public static Schedule ofEvent(LocalDateTime startAt, LocalDateTime endAt, String title,
                                   String description, User writer) {
        return Schedule.builder()
                       .startAt(startAt)
                       .endAt(endAt)
                       .title(title)
                       .description(description)
                       .writer(writer)
                       .scheduleType(ScheduleType.EVENT)
                       .createdAt(LocalDateTime.now())
                       .build();
    }

    public static Schedule ofTask(String title, String description, LocalDateTime taskAt,
                                  User writer) {
        return Schedule.builder()
                       .startAt(taskAt)
                       .title(title)
                       .description(description)
                       .writer(writer)
                       .scheduleType(ScheduleType.EVENT)
                       .createdAt(LocalDateTime.now())
                       .build();
    }

    public static Schedule ofNotification(LocalDateTime notifyAt, String title, User writer) {
        return Schedule.builder()
                       .startAt(notifyAt)
                       .title(title)
                       .writer(writer)
                       .scheduleType(ScheduleType.EVENT)
                       .createdAt(LocalDateTime.now())
                       .build();
    }

    public Task toTask() {
        return new Task(id, writer, title, description, startAt, createdAt);
    }

    public Event toEvent() {
        return new Event(id, startAt, endAt, createdAt, title, description, writer);
    }

    public Notification toNotification() {
        return new Notification(id, writer, title, startAt);
    }
}
