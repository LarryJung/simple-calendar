package com.larry.lallender.lallender.domain.repository;

import com.larry.lallender.lallender.domain.entity.Engagement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EngagementRepository extends JpaRepository<Engagement, Long> {
    // TODO 쿼리로 어느정도 필터링을 해서 가져오는게 낫지 않을까?
    List<Engagement> findAllByAttendeeIdIn(List<Long> attendeeIds);
}
