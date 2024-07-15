package com.study.board.controller;

import com.study.board.dto.BoardDTO;
import com.study.board.dto.UserDTO;
import com.study.board.entity.Board;
import com.study.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

// 화면 보이는 Controller
@Controller
@RequiredArgsConstructor
public class HomController {

    private final BoardService boardService;

    @GetMapping("/")
    public String showlList(){
        return "redirect:/boards";
    }
    @GetMapping("/boards")
    public String list(@RequestParam(name = "page",defaultValue = "0") int page,
                       @RequestParam(name = "size", defaultValue = "10") int size,
                       Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BoardDTO.Response> list = boardService.pageList(pageable);

        model.addAttribute("boards", list);

        return "boards";
    }

    @GetMapping("/boards/{id}")
    public String showOne(@PathVariable int id, Model model){
        model.addAttribute("boardDTO",boardService.findById(id));
        return "boards/detail";
    }

    // 회원 가입
    @GetMapping("/users/signUp")
    public String showSingUp(Model model) {
        model.addAttribute("userDTO",new UserDTO.Request());
        return "users/signUp";
    }

    @GetMapping("/users/signIn")
    public String showSignIn(){
        return "users/signIn";
    }
}
