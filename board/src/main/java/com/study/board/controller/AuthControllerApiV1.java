package com.study.board.controller;

import com.study.board.dto.UserDTO;
import com.study.board.exception.BadRequestException;
import com.study.board.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthControllerApiV1 {
    private final UserService userService;

    // 회원가입
    @PostMapping("/signUp")
    public HttpEntity<?> signUp(@RequestBody @Valid UserDTO.Request userDTO, Errors error) {
        // Validation 중 에러 발생 시, BadRequestException 발생
        if (error.hasErrors()) {
            throw new BadRequestException(error.getAllErrors().get(0).getDefaultMessage());
        }
        return userService.join(userDTO);
    }

    @PostMapping("/signIn")
    public HttpEntity<?> login(@RequestBody @Valid UserDTO.logInRequest userDTO, Errors error) {
        if (error.hasErrors()) {
            throw new BadRequestException(error.getAllErrors().get(0).getDefaultMessage());
        }
        return userService.login(userDTO);
    }

}
