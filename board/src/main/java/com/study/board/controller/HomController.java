package com.study.board.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.study.board.dto.BoardDTO;
import com.study.board.dto.UserDTO;
import com.study.board.entity.User;
import com.study.board.exception.BadRequestException;
import com.study.board.security.auth.JwtProvider;
import com.study.board.service.BoardService;
import com.study.board.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

// 화면 보이는 Controller
@Controller
@RequiredArgsConstructor
public class HomController {

    private final BoardService boardService;
    private final UserService userService;
    private final JwtProvider jwtProvider;

    public void getUser(@CookieValue(name =  "ACCESS-TOKEN", required = false) String token, Model model){
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

    @GetMapping("/")
    public String showList() {
        return "redirect:/boards";
    }

    @GetMapping("/boards")
    public String list(@RequestParam(name = "page", defaultValue = "0") int page,
                       @RequestParam(name = "size", defaultValue = "10") int size,
                       Model model,@CookieValue(name =  "ACCESS-TOKEN", required = false) String token) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BoardDTO.Response> list = boardService.pageList(pageable);

        model.addAttribute("boards", list);
        getUser(token,model);
        return "boards";
    }

    @GetMapping("/boards/showOne/{id}")
    public String showOne(@PathVariable int id, Model model,@CookieValue(name =  "ACCESS-TOKEN", required = false) String token) {
        model.addAttribute("boardDTO", boardService.findById(id));
        getUser(token,model);
        return "boards/detail";
    }

    // 회원 가입
    @GetMapping("/users/signUp")
    public String showSingUp(Model model, @CookieValue(name =  "ACCESS-TOKEN", required = false) String token) {
        getUser(token,model);
        return "users/signUp";
    }

    @GetMapping("/users/signIn")
    public String showSignIn(Model model, @CookieValue(name =  "ACCESS-TOKEN", required = false) String token) {
        getUser(token,model);
        return "users/signIn";
    }

    @GetMapping("/boards/new")
    public String showWrite(Model model,@CookieValue(name =  "ACCESS-TOKEN", required = false) String token){
        model.addAttribute("boardDTO",new BoardDTO.Request());
        getUser(token,model);
        return "boards/new";
    }
}
