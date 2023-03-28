package com.commbti.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequestMapping("/")
@Controller
public class RootController {

    @GetMapping
    public String getRootPage() {
        return "forward:/bulletin-board?page=1&size=10";
    }
}
