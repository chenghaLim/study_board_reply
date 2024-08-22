package com.study.board.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.board.dto.BoardDTO;
import com.study.board.dto.CommentDTO;
import com.study.board.entity.Board;
import com.study.board.entity.Comment;
import com.study.board.entity.QComment;
import com.study.board.repository.BoardRepository;
import com.study.board.repository.CommentRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public Page<BoardDTO.Response> pageList(Pageable pageable) {
        Page<Board> boardEntityList = boardRepository.findAll(pageable);
        return boardEntityList.map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public BoardDTO.Response getBoardWithComments(int boardId) {
        BoardDTO.Response response = convertToDto(boardRepository.findById(boardId));
//        Sort sort = Sort.by(Sort.Order.desc("ref"), Sort.Order.asc("step"));
//        List<Comment> byBoardId = commentRepository.findByBoardId(boardId, sort);
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        QComment comment = QComment.comment;
        List<Comment> byBoardId = jpaQueryFactory.selectFrom(comment)
                .where(comment.board.id.eq(boardId))
                .orderBy(comment.ref.desc(), comment.step.asc())
                .fetch();
        response.setComments(convertCommentsToDto(byBoardId));
        return response;
    }


    private BoardDTO.Response convertToDto(Board board) {
        BoardDTO.Response response = new BoardDTO.Response();
        response.setId(board.getId());
        response.setTitle(board.getTitle());
        response.setContent(board.getContent());
        response.setCreatedAt(board.getCreatedAt());
        response.setUpdatedAt(board.getUpdatedAt());
        response.setName(board.getUser().getName());
        return response;
    }

    private List<CommentDTO.Response> convertCommentsToDto(List<Comment> comments) {
        return comments.stream()
                .map(this::convertCommentToDto)
                .collect(Collectors.toList());
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

        return response;
    }
}

