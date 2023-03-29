package com.commbti.domain.bulletinboard.service;

import com.commbti.domain.bulletinboard.dto.BoardPageDto;
import com.commbti.domain.bulletinboard.entity.Bulletin;
import com.commbti.domain.bulletinboard.repository.BulletinBoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final BulletinBoardRepository bulletinBoardRepository;

    // 게시글 조회(전체)
    public BoardPageDto findBoardPage(int page, int size) {
        log.trace("findBoardListPage 호출: page={}, size={}", page, size);
        // 기본값: page=0; size= 10;
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<Bulletin> boardPage = bulletinBoardRepository.findPageWithMemberMbti(pageRequest);

        log.debug("Page<Bulletin>: totalPages={}, getTotalElements={}, hasNext={}, hasPrev={}", boardPage.getTotalPages(), boardPage.getTotalElements(), boardPage.hasNext(), boardPage.hasPrevious());

        BoardPageDto response = BoardPageDto.toPageDto(boardPage);
        log.debug("BoardPageDto.PageInfo: startNumber={}, endNumber={}", response.getPageInfo().getStartNumber(), response.getPageInfo().getEndNumber());

        log.trace("findBoardListPage 정상");
        return response;
    }
}
