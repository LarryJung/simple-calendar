package com.larry.lallender.lallender.domain.repository;

import com.larry.lallender.lallender.domain.entity.Engagement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EngagementRepository extends JpaRepository<Engagement, Long> {
    List<Engagement> findAllByAttendeeIdInAndSchedule_EndAtBefore(List<Long> attendeeIds, LocalDateTime startAt);
}
