package com.study.board.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.study.board.dto.BoardDTO;
import com.study.board.dto.UserDTO;
import com.study.board.exception.BadRequestException;
import com.study.board.security.auth.JwtProvider;
import com.study.board.service.CustomUserByIdService;
import com.study.board.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @GetMapping("/boards/new")
    public String showWrite(Model model, @CookieValue(name = "ACCESS-TOKEN", required = false) String token) {
        try {
            getUser(token, model);
            UserDTO.Response user = (UserDTO.Response) model.getAttribute("user");

            if (user == null) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 부족합니다.");
            }

            model.addAttribute("boardDTO", new BoardDTO.Request());
            return "boards/new";
        } catch (BadRequestException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    public void getUser(@CookieValue(name = "ACCESS-TOKEN", required = false) String token, Model model) {
        if (token != null && !token.isEmpty()) {
            try {
                DecodedJWT jwt = jwtProvider.verify(token);
                int userId = jwtProvider.getUserIdFromToken(jwt);
                UserDTO.Response logInUser = userService.findById(userId);
                model.addAttribute("user", logInUser);
            } catch (JWTVerificationException e) {
                throw new BadRequestException("로그인한 유저 없음: " + e.getMessage());
            }
        }
    }

}
