package com.study.board.repository;

import com.study.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {

//    @Query("SELECT b FROM Board b JOIN FETCH b.user ORDER BY b.id DESC")
    Page<Board> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"user", "comments", "comments.user"})
    @Query("SELECT b FROM Board b WHERE b.id = :id")
    Board findById(@Param("id") int id);



}
