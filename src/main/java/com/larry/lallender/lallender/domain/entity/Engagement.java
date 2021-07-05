package com.larry.lallender.lallender.domain.entity;

import com.larry.lallender.lallender.dto.EngagementRes;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Engagement {
    @Id
    @GeneratedValue
    private Long id;

    @JoinColumn(name = "schedule_id")
    @ManyToOne(optional = false)
    private Schedule schedule;

    @JoinColumn(name = "attendee_id")
    @ManyToOne(optional = false)
    @Getter
    private User attendee;

    @Getter
    @Enumerated(EnumType.STRING)
    private EngagementStatus status;

    public Event getEvent() {
        return schedule.toEvent();
    }

    public Engagement accept() {
        this.status = EngagementStatus.ACCEPTED;
        return this;
    }

    public Engagement reject() {
        this.status = EngagementStatus.REJECTED;
        return this;
    }

    public static Engagement of(Event event, User attendee) {
        return Engagement.builder()
                         .schedule(event.getSchedule())
                         .attendee(attendee)
                         .status(EngagementStatus.REQUESTED)
                         .build();
    }

    public EngagementRes toRes() {
        return new EngagementRes(id, getEvent().toRes(), attendee.toRes(), status);
    }

    public boolean isOverlapped(LocalDate date) {
        return this.schedule.isOverlapped(date);
    }
}
