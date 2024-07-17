package com.study.board.controller;

import com.study.board.dto.UserDTO;
import com.study.board.exception.BadRequestException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReplyController {

    @PostMapping("/replys")
    public String write() {
        return "";
    }

    @PostMapping("/replys/{id}")
    public String CCOment() {
        return "";
    }

}
