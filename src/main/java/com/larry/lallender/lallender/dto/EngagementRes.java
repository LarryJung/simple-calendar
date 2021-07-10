package com.larry.lallender.lallender.dto;

import com.larry.lallender.lallender.domain.entity.RequestStatus;
import lombok.Data;

@Data
public class EngagementRes {
    private final Long id;
    private final EventRes event;
    private final UserRes attendee;
    private final RequestStatus status;
}
