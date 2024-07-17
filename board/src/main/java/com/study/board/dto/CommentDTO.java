package com.study.board.dto;

import com.study.board.entity.Board;
import com.study.board.entity.Comment;
import com.study.board.entity.User;
import lombok.*;

public class CommentDTO {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        private int id;
        private String comment;
        private String createdAt, updatedAt;
        private User user;
        private Board board;

        public Comment toEntity() {
            Comment comments = Comment.builder()
                    .id(id)
                    .comment(comment)
                    .user(user)
                    .board(board)
                    .build();

            return comments;
        }
    }

    @RequiredArgsConstructor
    @Getter
    public static class Response {
        private int id;
        private String comment;
        private String createdAt, updateAt;
        private String name;
        private int userId;
        private int boardId;

        public Response(Comment comment) {
            this.id = comment.getId();
            this.comment = comment.getComment();
            this.createdAt = comment.getCreatedAt();
            this.updateAt = comment.getUpdatedAt();
            this.name = comment.getUser().getName();
            this.userId = comment.getUser().getId();
            this.boardId = comment.getBoard().getId();
        }
    }
}
