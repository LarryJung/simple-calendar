package com.larry.lallender.lallender.domain.repository;

import com.larry.lallender.lallender.domain.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
