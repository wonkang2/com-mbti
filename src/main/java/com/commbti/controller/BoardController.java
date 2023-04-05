package com.commbti.controller;

import com.commbti.domain.bulletinboard.dto.BoardPageDto;
import com.commbti.domain.bulletinboard.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RequestMapping("/bulletin-board")
@RequiredArgsConstructor
@Controller
public class BoardController {
    private static final int BOARD_SIZE = 10;
    private final BoardService boardService;

    @GetMapping
    public String getBoardPage(@RequestParam("page") int page,
                               @RequestParam("size") int size,
                               Model model) {
        BoardPageDto response = boardService.findBoardPage(page, BOARD_SIZE);
        model.addAttribute("boardPage", response);
        return "/bulletin-board/board";
    }

}

