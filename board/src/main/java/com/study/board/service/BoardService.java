package com.study.board.service;

import com.study.board.dto.BoardDTO;
import com.study.board.entity.Board;
import com.study.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional(readOnly = true)
    public Page<BoardDTO.Response> pageList(Pageable pageable) {
        Page<Board> boardEntityList = boardRepository.findAll(pageable);
        return boardEntityList.map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public BoardDTO.Response findById(int id){
        return convertToDto(boardRepository.findById(id));
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
}

