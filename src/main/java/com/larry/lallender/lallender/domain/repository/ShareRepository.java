package com.larry.lallender.lallender.domain.repository;


import com.larry.lallender.lallender.domain.entity.RequestStatus;
import com.larry.lallender.lallender.domain.entity.Share;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShareRepository extends JpaRepository<Share, Long> {
    Optional<Share> findByIdAndToUserId(Long shareId, Long toUserId);

    List<Share> findAllByFromUserIdAndStatus(Long fromUserId,
                                             RequestStatus status);

    List<Share> findAllByToUserIdAndAndDirectionAndStatus(Long fromUserId,
                                                          Share.Direction direction,
                                                          RequestStatus status);
}
