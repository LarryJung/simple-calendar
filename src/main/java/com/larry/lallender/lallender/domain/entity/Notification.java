package com.larry.lallender.lallender.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
public class Notification {
    private Long id;
    private User writer;
    private String title;
    private LocalDateTime notifyAt;
//    private RepeatInfo repeatInfo; 은 없어도 되고, 반복설정을 가지고 요청이 오면 그만큼 flatten 해서 저장해버림;;
}
