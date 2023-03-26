package com.commbti.controller;

import com.commbti.domain.bulletinboard.dto.BoardResponseDto;
import com.commbti.domain.bulletinboard.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@RequestMapping("/bulletin-board")
@RequiredArgsConstructor
@Controller
public class BoardController {
    private static final int BOARD_SIZE = 10;
    private final BoardService boardService;

    @GetMapping
    public String getBoardPage(Model model) {
        log.trace("Board List 호출");
        List<BoardResponseDto> boardListPage = boardService.findBoardListPage(1, BOARD_SIZE);
        model.addAttribute("boardList", boardListPage);
        log.trace("Board List 정상");
        return "/bulletin-board/board";
    }


}

