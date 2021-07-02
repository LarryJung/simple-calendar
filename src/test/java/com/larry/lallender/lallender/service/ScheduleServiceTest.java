package com.larry.lallender.lallender.service;

import com.larry.lallender.lallender.domain.entity.Schedule;
import com.larry.lallender.lallender.domain.entity.Task;
import com.larry.lallender.lallender.domain.entity.User;
import com.larry.lallender.lallender.domain.repository.EngagementRepository;
import com.larry.lallender.lallender.domain.repository.ScheduleRepository;
import com.larry.lallender.lallender.dto.TaskCreateReq;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ScheduleServiceTest {
    private static final LocalDateTime time1 = LocalDateTime.of(2021, 7, 1, 0, 0, 0);
    private static final LocalDateTime time2 = LocalDateTime.of(2021, 7, 1, 12, 0, 0);
    private final ScheduleRepository scheduleRepository = Mockito.mock(ScheduleRepository.class);
    private final EngagementRepository engagementRepository =
            Mockito.mock(EngagementRepository.class);
    private final ScheduleService scheduleService = new ScheduleService(scheduleRepository,
                                                                        engagementRepository);

    @Test
    @DisplayName("할일을 생성한다.")
    void test1() {
        User writer = User.builder()
                          .id(1L)
                          .email("my@gmail.com")
                          .build();
        TaskCreateReq req = new TaskCreateReq("방청소", null, time1);

        when(scheduleRepository.save(any())).thenReturn(Schedule.ofTask(req.getTitle(),
                                                                        req.getDescription(),
                                                                        req.getTaskAt(),
                                                                        writer));
        Task task = scheduleService.createTask(writer, req);
        assertEquals("방청소", task.getTitle());
        assertEquals(1L,
                     task.getWriter()
                         .getId());
    }


}
