package com.commbti.global.event.listener;

import com.commbti.domain.bulletinboard.entity.Bulletin;
import com.commbti.domain.bulletinboard.repository.BulletinBoardRepository;
import com.commbti.global.event.BulletinViewEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class BulletinViewEventListener {

    private final BulletinBoardRepository bulletinBoardRepository;

    // TODO: 동시성 문제로 조회수가 누락되는 이슈가 있으나 현재 단계에서 치명적인 이슈가 아니라고 생각하여 추후 이슈를 해결할 예정.(DB LOCK)
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addViewCount(BulletinViewEvent bulletinEvent) {
        log.debug("조회수 증가 이벤트 호출");
        Bulletin foundBulletin = bulletinEvent.getBulletin();
        foundBulletin.addViewCount();
        bulletinBoardRepository.save(foundBulletin);
        log.debug("조회수 증가 이벤트 종료");
    }
}
