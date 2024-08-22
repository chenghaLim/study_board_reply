package com.study.board.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table
@Entity
@Setter
public class Comment extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content; // 댓글 내용

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private Long ref; // 최상위 부모 댓글 아이디

    @Column(nullable = false)
    private Long step;

    @Column(nullable = false)
    private int depth; // 대댓글의 깊이
}
