package com.larry.lallender.lallender.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ReplyEngagementReq {
    @NotNull
    private final Long engagementId;
    @NotNull
    private final RequestReplyType type;
}
