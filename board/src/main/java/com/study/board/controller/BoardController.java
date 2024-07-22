package com.study.board.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.study.board.dto.BoardDTO;
import com.study.board.dto.UserDTO;
import com.study.board.exception.BadRequestException;
import com.study.board.exception.UnauthorizedException;
import com.study.board.security.CustomUserDetails;
import com.study.board.security.auth.JwtProvider;
import com.study.board.service.BoardService;
import com.study.board.service.CustomUserByIdService;
import com.study.board.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BoardController {

   private final CustomUserByIdService customUserByIdService;
   private final JwtProvider jwtProvider;
   private final UserService userService;

    @PostMapping("/boards/new")
    public String write() {
        return "";
    }

    @GetMapping("/api/boards/new")
    @ResponseBody
    public ResponseEntity<String> newBoard(@AuthenticationPrincipal UserDetails userDetails) {
        // UserDetails가 null이면 인증되지 않은 요청입니다.
        if (userDetails == null) {
            throw new UnauthorizedException("인증되지 않은 요청입니다.", false);
        }

        // 인증된 사용자가 요청을 보냈다면, 원하는 응답을 반환합니다.
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
