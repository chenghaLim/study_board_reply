package com.study.board.repository;

import com.study.board.entity.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, QuerydslPredicateExecutor<Comment> {
    @EntityGraph(attributePaths = {"board", "user"})
    List<Comment> findByBoardId(int boardId, Sort sort);


    Comment findById(int id);

    Long countByRef(Long ref);

    @Modifying
    @Transactional
    @Query("update Comment c set c.step = c.step + 1 where c.ref = ?1 and c.step > ?2")
    int increaseStep(Long ref, Long step);

    @Modifying
    @Transactional
    @Query("update Comment c set c.step = c.step - 1 where c.ref = ?1 and c.step >= ?2")
    int decreaseStep(Long ref, Long step);


    @Query("SELECT COALESCE(MAX(c.step), 0) FROM Comment c WHERE c.ref = :ref AND c.depth = :depth")
    Long findMaxStepByRefAndDepth(@Param("ref") Long ref, @Param("depth") Integer depth);

    @Modifying
    @Query("update Comment c set c.ref = :ref where c.id = :id")
    void updateRefById(@Param("ref") Long ref, @Param("id") Long id);
}
