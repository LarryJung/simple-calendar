package com.larry.lallender.lallender.controller.api;

import com.larry.lallender.lallender.dto.SimpleScheduleDto;
import com.larry.lallender.lallender.exception.CalendarException;
import com.larry.lallender.lallender.exception.ErrorCode;
import com.larry.lallender.lallender.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

import java.util.List;

import static com.larry.lallender.lallender.service.LoginService.LOGIN_SESSION_KEY;

@RestController
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping("/api/schedules")
    public List<SimpleScheduleDto> getSchedules(HttpSession session) {
        final Long userId = (Long) session.getAttribute(LOGIN_SESSION_KEY);
        if (userId == null) {
            throw new CalendarException(ErrorCode.BAD_REQUEST);
        }
        return scheduleService.getSchedules(userId);
    }

}
