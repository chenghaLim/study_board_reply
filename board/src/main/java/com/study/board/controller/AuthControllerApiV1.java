package com.study.board.controller;

import com.study.board.dto.UserDTO;
import com.study.board.exception.BadRequestException;
import com.study.board.exception.ResDTO;
import com.study.board.security.CustomUserDetails;
import com.study.board.security.auth.JwtProvider;
import com.study.board.security.auth.JwtToken;
import com.study.board.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthControllerApiV1 {
    private final UserService userService;
    private final JwtProvider jwtProvider;

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
    public HttpEntity<?> login(@RequestBody UserDTO.logInRequest userDTO, Errors error){
        if (error.hasErrors()) {
            throw new BadRequestException(error.getAllErrors().get(0).getDefaultMessage());
        }
        return userService.login(userDTO);
    }

    @GetMapping("/logout")
    public HttpEntity<?> logout(@AuthenticationPrincipal CustomUserDetails logInUser, HttpServletRequest request) {
        String accessToken = jwtProvider.resolveToken(request);
        return userService.logOut(accessToken,logInUser.getUsername());
        // 로그아웃 성공 메시지 반환
    }
}
