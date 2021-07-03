package com.larry.lallender.lallender.service;

import com.larry.lallender.lallender.domain.entity.*;
import com.larry.lallender.lallender.domain.entity.dto.EventWithEngagement;
import com.larry.lallender.lallender.domain.entity.dto.Notification;
import com.larry.lallender.lallender.domain.entity.dto.Task;
import com.larry.lallender.lallender.domain.repository.EngagementRepository;
import com.larry.lallender.lallender.domain.repository.ScheduleRepository;
import com.larry.lallender.lallender.dto.EventCreateReq;
import com.larry.lallender.lallender.dto.NotificationCreateReq;
import com.larry.lallender.lallender.dto.TaskCreateReq;
import com.larry.lallender.lallender.util.TimeUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ScheduleServiceTest {
    private static final LocalDateTime time1 = LocalDateTime.of(2021, 7, 1, 0, 0, 0);
    private static final LocalDateTime time2 = LocalDateTime.of(2021, 7, 1, 12, 0, 0);
    private static final User user1 = User.builder()
                                          .id(1L)
                                          .email("my@gmail.com")
                                          .build();
    private final ScheduleRepository scheduleRepository = Mockito.mock(ScheduleRepository.class);
    private final EngagementRepository engagementRepository =
            Mockito.mock(EngagementRepository.class);
    private final UserService userService = Mockito.mock(UserService.class);
    private final ScheduleService scheduleService = new ScheduleService(scheduleRepository,
                                                                        engagementRepository,
                                                                        userService);

    @Test
    @DisplayName("할일을 생성한다.")
    void test1() {
        final User writer = user1;
        final TaskCreateReq req = new TaskCreateReq("방청소", null, time1);

        when(scheduleRepository.save(any())).thenReturn(Schedule.ofTask(req.getTitle(),
                                                                        req.getDescription(),
                                                                        req.getTaskAt(),
                                                                        writer));
        final Task task = scheduleService.createTask(writer, req);
        assertEquals("방청소", task.getTitle());
        assertEquals(1L,
                     task.getWriter()
                         .getId());
    }

    @Test
    @DisplayName("이벤트 생성한다.")
    void test2() {
        final User writer = user1;

        when(engagementRepository.findAllByAttendeeIdInAndSchedule_EndAtBefore(any(), any()))
                .thenReturn(List.of());
        when(scheduleRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(engagementRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(userService.findById(any())).thenAnswer(invocation -> User.builder()
                                                                       .id(invocation.getArgument(0))
                                                                       .build());

        final EventWithEngagement result = scheduleService.createEvent(writer,
                                                                       new EventCreateReq(time1,
                                                                                          time2,
                                                                                          "작당모의",
                                                                                          "비밀",
                                                                                          List.of(2L,
                                                                                                  3L)));
        assertEquals(result.getEvent()
                           .getTitle(), "작당모의");
        assertEquals(result.getEvent()
                           .getWriter()
                           .getId(), 1L);
        assertEquals(result.getEngagements()
                           .stream()
                           .map(e -> e.getAttendee()
                                      .getId())
                           .collect(
                                   Collectors.toList()), List.of(2L, 3L));

    }

    @Test
    @DisplayName("이벤트 생성한다. - 겹치는 이벤트가 있지만 수락 상태는 아님")
    void test3() {
        final User writer = user1;
        when(engagementRepository.findAllByAttendeeIdInAndSchedule_EndAtBefore(any(), any()))
                .thenReturn(List.of(
                        Engagement.of(Schedule.ofEvent(time1,
                                                       time2,
                                                       null,
                                                       null,
                                                       null)
                                              .toEvent(),
                                      User.builder()
                                          .id(2L)
                                          .build()),
                        Engagement.of(Schedule.ofEvent(time2.plusMinutes(5),
                                                       time2.plusMinutes(10),
                                                       null,
                                                       null,
                                                       null)
                                              .toEvent(),
                                      User.builder()
                                          .id(3L)
                                          .build())
                            )
                );
        when(scheduleRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(engagementRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(userService.findById(any())).thenAnswer(invocation -> User.builder()
                                                                       .id(invocation.getArgument(0))
                                                                       .build());

        final EventWithEngagement result = scheduleService.createEvent(writer,
                                                                       new EventCreateReq(time1,
                                                                                          time2,
                                                                                          "작당모의",
                                                                                          "비밀",
                                                                                          List.of(2L,
                                                                                                  3L)));
        assertEquals(result.getEvent()
                           .getTitle(), "작당모의");
        assertEquals(result.getEvent()
                           .getWriter()
                           .getId(), 1L);
        assertEquals(result.getEngagements()
                           .stream()
                           .map(e -> e.getAttendee()
                                      .getId())
                           .collect(
                                   Collectors.toList()), List.of(2L, 3L));
    }

    @Test
    @DisplayName("이벤트 생성한다. - 겹치는 이벤트가 있지만 수락 상태는 아님")
    void test4() {
        final User writer = user1;
        when(engagementRepository.findAllByAttendeeIdInAndSchedule_EndAtBefore(any(), any()))
                .thenReturn(List.of(
                        Engagement.of(Schedule.ofEvent(time1,
                                                       time2,
                                                       null,
                                                       null,
                                                       null)
                                              .toEvent(),
                                      User.builder()
                                          .id(2L)
                                          .build())
                                  .accept(),
                        Engagement.of(Schedule.ofEvent(time2.plusMinutes(5),
                                                       time2.plusMinutes(10),
                                                       null,
                                                       null,
                                                       null)
                                              .toEvent(),
                                      User.builder()
                                          .id(3L)
                                          .build())
                            )
                );
        when(scheduleRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(engagementRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(userService.findById(any())).thenAnswer(invocation -> User.builder()
                                                                       .id(invocation.getArgument(0))
                                                                       .build());

        final RuntimeException ex = assertThrows(RuntimeException.class,
                                                 () -> scheduleService.createEvent(writer,
                                                                                   new EventCreateReq(
                                                                                           time1,
                                                                                           time2,
                                                                                           "작당모의",
                                                                                           "비밀",
                                                                                           List.of(2L,
                                                                                                   3L))));
        assertEquals("cannot create event - time overlap", ex.getMessage());
    }

    @Test
    @DisplayName("알림 생성한다. - 반복 없음")
    void test5() {
        final User writer = user1;
        when(scheduleRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        final List<Notification> notifications = scheduleService.createNotification(writer,
                                                                                    new NotificationCreateReq(
                                                                                            "스프링 공부",
                                                                                            time1,
                                                                                            null));
        assertEquals(1, notifications.size());
        assertEquals(time1,
                     notifications.get(0)
                                  .getNotifyAt());
    }

    @Test
    @DisplayName("알림 생성한다. - 일 반복")
    void test6() {
        final User writer = user1;
        when(scheduleRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        final List<Notification> notifications = scheduleService.createNotification(writer,
                                                                                    new NotificationCreateReq(
                                                                                            "스프링 공부",
                                                                                            time1,
                                                                                            new NotificationCreateReq.RepeatInfo(
                                                                                                    new NotificationCreateReq.RepeatPeriod(
                                                                                                            1,
                                                                                                            TimeUnit.DAY),
                                                                                                    3
                                                                                            )));
        assertEquals(3, notifications.size());
        assertEquals(List.of(time1, time1.plusDays(1), time1.plusDays(2)),
                     notifications.stream()
                                  .map(Notification::getNotifyAt)
                                  .sorted()
                                  .collect(Collectors.toList()));
    }
}
