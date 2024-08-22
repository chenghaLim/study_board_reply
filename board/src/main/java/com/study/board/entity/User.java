package com.study.board.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Table(name = "user")
public class User extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 50, unique = true)
    private String email;

    @Column(length = 60)
    private String password;

    @Column(nullable = false)
    private String name;

    private Role role;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Board> boards;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Comment> comments;

}
