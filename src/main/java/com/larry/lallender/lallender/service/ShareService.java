package com.larry.lallender.lallender.service;

import com.larry.lallender.lallender.domain.entity.RequestStatus;
import com.larry.lallender.lallender.domain.entity.Share;
import com.larry.lallender.lallender.domain.repository.ShareRepository;
import com.larry.lallender.lallender.dto.AuthUser;
import com.larry.lallender.lallender.dto.RequestReplyType;
import com.larry.lallender.lallender.exception.CalendarException;
import com.larry.lallender.lallender.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ShareService {
    private final UserService userService;
    private final ShareRepository shareRepository;

    @Transactional
    public Share createShare(Long fromUserId, Long toUserId, Share.Direction direction) {
        if (userService.isExistedUser(toUserId)) {
            return shareRepository.save(Share.builder()
                                             .fromUserId(fromUserId)
                                             .toUserId(toUserId)
                                             .direction(direction)
                                             .createdAt(LocalDateTime.now())
                                             .status(RequestStatus.REQUESTED)
                                             .build());
        } else {
            throw new CalendarException(ErrorCode.BAD_REQUEST);
        }
    }

    @Transactional
    public void replyToShareRequest(Long shareId, Long toUserId, RequestReplyType type) {
        shareRepository.findByIdAndToUserId(shareId, toUserId)
                       .filter(Share::isRequested)
                       .map(share -> {
                           switch (type) {
                               case ACCEPT:
                                   return share.accept();
                               case REJECT:
                                   return share.reject();
                               default:
                                   return null;
                           }
                       })
                       .orElseThrow(() -> new CalendarException(ErrorCode.BAD_REQUEST));
    }

    @Transactional
    public Set<Long> findSharedUserIdsByUser(AuthUser authUser) {
        return Stream.concat(shareRepository.findAllByFromUserIdAndStatus(authUser.getId(),
                                                                          RequestStatus.ACCEPTED)
                                            .stream()
                                            .map(Share::getToUserId),
                             shareRepository.findAllByToUserIdAndAndDirectionAndStatus(authUser.getId(),
                                                                                       Share.Direction.BI_DIRECTION,
                                                                                       RequestStatus.ACCEPTED)
                                            .stream()
                                            .map(Share::getFromUserId))
                     .collect(Collectors.toSet());
    }
}
