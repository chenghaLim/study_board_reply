package com.study.board.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.study.board.entity.Board;
import com.study.board.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


public class BoardDTO {

    /**
     * 게시글의 등록과 수정을 처리할 요청(Request) 클래스
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {

        private int id;
        private String title;
        private String content;
        private String createdAt, updatedAt;
        private User user;


        /* Dto -> Entity */
        public Board toEntity() {
            Board board = Board.builder()
                    .id(id)
                    .title(title)
                    .content(content)
                    .user(user)
                    .build();

            return board;
        }
    }

    @Data
    public static class Response {
        private int id;
        private String title;
        private String content;
        private String createdAt, updatedAt;
        private int userId;
        private String name;
        private List<CommentDTO.Response> comments;

    }
}
