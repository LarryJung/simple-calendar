package com.larry.lallender.lallender.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor
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

    public static Engagement of(Event event, User attendee) {
        return Engagement.builder()
                         .schedule(Schedule.ofEvent(event))
                         .attendee(attendee)
                         .status(EngagementStatus.REQUESTED)
                         .build();
    }
}
