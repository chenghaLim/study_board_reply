package com.study.board.repository;

import com.study.board.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByEmail(String email);

    // 스프링의 캐시 추상화
    Optional<User> findByEmail(String email);

    Optional<User> findByName(String name);

    boolean existsByName(String name);

    Optional<User> findById(int id);
}
