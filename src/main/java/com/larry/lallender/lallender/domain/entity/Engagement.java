package com.larry.lallender.lallender.domain.entity;

import lombok.*;

@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Engagement {
    private Long id;
    private Schedule schedule;

    @Getter
    private User attendee;

    @Getter
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
