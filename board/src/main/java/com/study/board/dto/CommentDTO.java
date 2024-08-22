package com.study.board.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.study.board.entity.Board;
import com.study.board.entity.Comment;
import com.study.board.entity.User;
import lombok.*;

public class CommentDTO {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class Request {
        private Long id;
        private String content;
        private String createdAt, updatedAt;
        private User user;
        private Board board;
        private Long ref;
        private Long step;
        private int depth;
        private int boardId;

        public Comment toEntity() {
            Comment comments = Comment.builder()
                    .id(id)
                    .content(content)
                    .user(user)
                    .board(board)
                    .ref(ref)
                    .step(step)
                    .depth(depth)
                    .build();

            return comments;
        }
    }

    @RequiredArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class Response {
        private Long id;
        private String content;
        private String createdAt, updatedAt;
        private String name;
        private int userId;
        private int boardId;
        private Long ref;
        private Long step;
        private int depth;

    }
}
