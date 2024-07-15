package com.study.board.controller;

import com.study.board.dto.UserDTO;
import com.study.board.entity.Board;
import com.study.board.service.BoardService;
import com.study.board.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;


    @PostMapping("/users")
    public String signUp(@ModelAttribute UserDTO.Request userDTO){
        return "redirect:/users/signIn";
    }

    @PostMapping("users/auth")
    public String auth(){
        return "redirect:/boards";
    }


}
