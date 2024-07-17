package com.study.board.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.study.board.dto.BoardDTO;
import com.study.board.dto.UserDTO;
import com.study.board.exception.BadRequestException;
import com.study.board.security.auth.JwtProvider;
import com.study.board.service.BoardService;
import com.study.board.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final JwtProvider jwtProvider;
    private final UserService userService;

    @PostMapping("/boards/new")
    public String write() {
        return "";
    }

}
