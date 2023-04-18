package com.commbti.domain.bulletinboard.service;

import com.commbti.domain.bulletinboard.dto.BulletinResponseDto;
import com.commbti.domain.bulletinboard.entity.Bulletin;
import com.commbti.domain.bulletinboard.repository.BulletinBoardRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
public class ViewTest {

    @Autowired
    private BulletinService bulletinService;


    @DisplayName("조회수 증가 동시성 문제 확인용 코드")
    @Test
    void addViewCountTest() throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(10);
        Long beforeViewCount = bulletinService.findOne(1L).getViewCount();
        System.out.println("beforeViewCount: " + beforeViewCount);
        for (int i = 0; i < 10; i++) {
            service.execute(() -> {
                try {
                    bulletinService.findOne(1L, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        Thread.sleep(10000);
        Long afterViewCount = bulletinService.findOne(1L).getViewCount();
        System.out.println("afterViewCount: " + afterViewCount);
//        Assertions.assertThat(afterViewCount - beforeViewCount).isSameAs(10);
    }
}
