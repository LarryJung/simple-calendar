package com.larry.lallender.lallender.service;

import com.larry.lallender.lallender.domain.entity.EngagementStatus;
import com.larry.lallender.lallender.domain.repository.EngagementRepository;
import com.larry.lallender.lallender.dto.AuthUser;
import com.larry.lallender.lallender.dto.EngagementReplyType;
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
    public EngagementStatus update(AuthUser authUser,
                                   Long engagementId,
                                   EngagementReplyType type) {
        return engagementRepository.findById(engagementId)
                                   .map(engagement -> {
                                       if (!engagement.getAttendee()
                                                      .getId()
                                                      .equals(authUser.getId())) {
                                           return null;
                                       }
                                       switch (type) {
                                           case ACCEPT:
                                               return engagement.accept();
                                           case REJECT:
                                               return engagement.reject();
                                           default:
                                               throw new CalendarException(ErrorCode.BAD_REQUEST);
                                       }
                                   })
                                   .orElseThrow(() -> new CalendarException(ErrorCode.BAD_REQUEST))
                                   .getStatus();
    }
}
