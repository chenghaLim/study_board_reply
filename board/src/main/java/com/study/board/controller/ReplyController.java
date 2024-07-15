package com.study.board.controller;

import com.study.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ReplyController {

  @PostMapping("/replys")
    public String write(){
      return "";
  }

  @PostMapping("/replys/{id}")
    public String CCOment(){
      return "";
  }

}
