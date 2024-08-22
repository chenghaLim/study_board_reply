package com.study.board.controller;

import com.study.board.dto.CommentDTO;
import com.study.board.entity.Board;
import com.study.board.exception.BadRequestException;
import com.study.board.exception.ResDTO;
import com.study.board.repository.BoardRepository;
import com.study.board.security.auth.JwtProvider;
import com.study.board.service.BoardService;
import com.study.board.service.CommentService;
import com.study.board.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CommentApiController {

    private final CommentService commentService;
    private final JwtProvider jwtProvider;
    private final UserService userService;
    private final BoardService boardService;
    private final BoardRepository boardRepository;

    @PostMapping("/replys")
    public HttpEntity<?> write(@RequestBody @Valid CommentDTO.Request commentDTO, Errors error, @CookieValue(name = "ACCESS-TOKEN", required = false) String token) {
        // 새글일 경우
        try {
            if (error.hasErrors()) {
                throw new BadRequestException(error.getAllErrors().get(0).getDefaultMessage());
            }

            commentDTO.setDepth(0);
            commentDTO.setUser(userService.getUser(token));
            commentDTO.setBoard(boardRepository.findById(commentDTO.getBoardId()));
            commentDTO.setStep(1L);
            commentDTO.setRef(0L);
            log.info("Inserted CommentDTO: {}", commentDTO);
            Long insert = commentService.insert(commentDTO);
            commentDTO.setRef(insert);
            commentService.updateRef(commentDTO);
            return new ResponseEntity<>(
                    ResDTO.builder()
                            .code(0)
                            .message("comment insert success")
                            .build(),
                    HttpStatus.OK);
        } catch (BadRequestException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping("/replys/{id}")
    public HttpEntity<?> CCOment(@PathVariable int id,
                                 @RequestBody @Valid CommentDTO.Request commentDTO, Errors error
            , @CookieValue(name = "ACCESS-TOKEN", required = false) String token) {
        try {
            if (error.hasErrors()) {
                throw new BadRequestException(error.getAllErrors().get(0).getDefaultMessage());
            }

            CommentDTO.Response parentCommentDTO = commentService.findById(id);
            log.info("id = " + id + ",parentCommentDTO = " + parentCommentDTO);

            commentDTO.setRef(parentCommentDTO.getRef());
            commentDTO.setDepth(parentCommentDTO.getDepth() + 1);
            commentDTO.setUser(userService.getUser(token));
            Board byId = boardRepository.findById(parentCommentDTO.getBoardId());
            commentDTO.setBoard(byId);
            Long validStep = commentService.getValidStep(parentCommentDTO);
            Long step = null;

            if (validStep == null) {
                step = commentService.countByRef(parentCommentDTO.getRef()) + 1;
            } else {
                step = validStep + 1;
                commentService.increaseStep(parentCommentDTO, validStep);
            }

            commentDTO.setStep(step);
            commentService.insert(commentDTO);
            return new ResponseEntity<>(
                    ResDTO.builder()
                            .code(0)
                            .message("comment insert success")
                            .build(),
                    HttpStatus.OK);
        } catch (BadRequestException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }


}
