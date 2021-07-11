package com.larry.lallender.lallender.domain.entity;

import com.larry.lallender.lallender.dto.EngagementRes;
import com.larry.lallender.lallender.util.Period;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "engagements")
public class Engagement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private RequestStatus status;

    public Event getEvent() {
        return schedule.toEvent();
    }

    public boolean isDecided() {
        return this.status != RequestStatus.REQUESTED;
    }
    public Engagement accept() {
        this.status = RequestStatus.ACCEPTED;
        return this;
    }

    public Engagement reject() {
        this.status = RequestStatus.REJECTED;
        return this;
    }

    public static Engagement of(Event event, User attendee) {
        return Engagement.builder()
                         .schedule(event.getSchedule())
                         .attendee(attendee)
                         .status(RequestStatus.REQUESTED)
                         .build();
    }

    public EngagementRes toRes() {
        return new EngagementRes(id, getEvent().toRes(), attendee.toRes(), status);
    }

    public boolean isOverlapped(LocalDate date) {
        return this.schedule.isOverlapped(date);
    }

    public boolean isOverlapped(Period period) {
        return this.schedule.isOverlapped(period);
    }
}
