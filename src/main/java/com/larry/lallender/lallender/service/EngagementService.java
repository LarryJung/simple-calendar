package com.larry.lallender.lallender.service;

import com.larry.lallender.lallender.domain.entity.Engagement;
import com.larry.lallender.lallender.domain.entity.RequestStatus;
import com.larry.lallender.lallender.domain.repository.EngagementRepository;
import com.larry.lallender.lallender.dto.AuthUser;
import com.larry.lallender.lallender.dto.ReplyEngagementReq;
import com.larry.lallender.lallender.exception.CalendarException;
import com.larry.lallender.lallender.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EngagementService {
    private final EngagementRepository engagementRepository;

    @Transactional
    public RequestStatus update(AuthUser authUser,
                                ReplyEngagementReq replyEngagementReq) {
        return engagementRepository.findById(replyEngagementReq.getEngagementId())
                                   .filter(Engagement::isRequested)
                                   .map(engagement -> {
                                       if (!engagement.getAttendee()
                                                      .getId()
                                                      .equals(authUser.getId())) {
                                           return null;
                                       }
                                       switch (replyEngagementReq.getType()) {
                                           case ACCEPT:
                                               return engagement.accept();
                                           case REJECT:
                                               return engagement.reject();
                                           default:
                                               return null;
                                       }
                                   })
                                   .orElseThrow(() -> new CalendarException(ErrorCode.BAD_REQUEST))
                                   .getStatus();
    }
}
