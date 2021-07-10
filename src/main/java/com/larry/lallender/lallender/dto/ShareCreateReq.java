package com.larry.lallender.lallender.dto;

import com.larry.lallender.lallender.domain.entity.Share;
import lombok.Data;

@Data
public class ShareCreateReq {
    private final Long toUserId;
    private final Share.Direction direction;
}
