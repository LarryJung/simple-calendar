package com.larry.lallender.lallender.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.security.PublicKey;
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

    @Enumerated(value = EnumType.STRING)
    private ScheduleType scheduleType;
    private LocalDateTime createdAt;

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
                       .scheduleType(ScheduleType.TASK)
                       .createdAt(LocalDateTime.now())
                       .build();
    }

    public static Schedule ofNotification(LocalDateTime notifyAt, String title, User writer) {
        return Schedule.builder()
                       .startAt(notifyAt)
                       .title(title)
                       .writer(writer)
                       .scheduleType(ScheduleType.NOTIFICATION)
                       .createdAt(LocalDateTime.now())
                       .build();
    }

    public Task toTask() {
        return new Task(this, writer, title, description, startAt);
    }

    public Event toEvent() {
        return new Event(this, startAt, endAt, createdAt, title, description, writer);
    }

    public Notification toNotification() {
        return new Notification(this, writer, title, startAt);
    }
}
