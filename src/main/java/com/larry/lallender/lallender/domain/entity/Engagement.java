package com.larry.lallender.lallender.domain.entity;

import com.larry.lallender.lallender.domain.entity.dto.EngagementStatus;
import com.larry.lallender.lallender.domain.entity.dto.Event;
import lombok.*;

import javax.persistence.*;

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

    public static Engagement of(Event event, User attendee) {
        return Engagement.builder()
                         .schedule(Schedule.ofEvent(event))
                         .attendee(attendee)
                         .status(EngagementStatus.REQUESTED)
                         .build();
    }

}
