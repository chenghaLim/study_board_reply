package com.study.board.service;

import com.study.board.dto.CommentDTO;
import com.study.board.entity.Comment;
import com.study.board.repository.CommentRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public CommentDTO.Response findById(int id) {
        return convertCommentToDto(commentRepository.findById(id));
    }

    public Long countByRef(Long ref) {
        return commentRepository.countByRef(ref);
    }

    public int increaseStep(CommentDTO.Response commentDTO,Long step) {
        return commentRepository.increaseStep(commentDTO.getRef(), step);
    }

    @Transactional
    public Long getValidStep(CommentDTO.Response commentDTO) {
        return commentRepository.findMaxStepByRefAndDepth(commentDTO.getRef(), commentDTO.getDepth() + 1);
    }

    public int decreaseStep(CommentDTO.Response commentDTO) {
        return commentRepository.decreaseStep(commentDTO.getRef(), commentDTO.getStep());
    }

    @Transactional
    public Long insert(@Valid CommentDTO.Request commentDTO) {
        Comment save = commentRepository.save(commentDTO.toEntity());
        return save.getId();
    }

    @Transactional
    public void updateRef(CommentDTO.Request comment){
        commentRepository.updateRefById(comment.getRef(),comment.getId());
    }

    private CommentDTO.Response convertCommentToDto(Comment comment) {
        CommentDTO.Response response = new CommentDTO.Response();
        response.setId(comment.getId());
        response.setContent(comment.getContent());
        response.setCreatedAt(comment.getCreatedAt());
        response.setUpdatedAt(comment.getUpdatedAt());
        response.setUserId(comment.getUser().getId());
        response.setName(comment.getUser().getName());
        response.setRef(comment.getRef());
        response.setStep(comment.getStep());
        response.setDepth(comment.getDepth());
        response.setBoardId(comment.getBoard().getId());

        return response;
    }
}
