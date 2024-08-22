package com.study.board;

import com.study.board.entity.Board;
import com.study.board.entity.Comment;
import com.study.board.entity.User;
import com.study.board.repository.BoardRepository;
import com.study.board.repository.CommentRepository;
import com.study.board.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class BoardApplicationTests {
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void contextLoads() {
        for (Long i = 1L; i <= 10; i++) {
            Comment comment = new Comment();
            comment.setContent("Test Comment " + i);
            Board byId = boardRepository.findById(300);
            comment.setBoard(byId);
            Optional<User> byId1 = userRepository.findById(4);
            comment.setUser(byId1.get());
            comment.setDepth(0);
            comment.setRef(i);
            comment.setStep(1L);
            commentRepository.save(comment);
        }

    }
//    @Test
//    void depthEx(){
//        for(Long i = 1L ; i<100; i++){
//            Comment comment = new Comment();
//            comment.setContent("Test Comment 10 - depth " + i );
//            Board byId = boardRepository.findById(300);
//            comment.setBoard(byId);
//            Optional<User> byId1 = userRepository.findById(4);
//            comment.setUser(byId1.get());
//            comment.setDepth(i);
//            comment.setRef(10L);
//            comment.setStep(i);
//            commentRepository.save(comment);
//        }
//    }

//    @Test
//    void depthEx2(){
//
//            Comment comment = new Comment();
//            comment.setContent("Test Comment 10 - depth " + 99 );
//            Board byId = boardRepository.findById(300);
//            comment.setBoard(byId);
//            Optional<User> byId1 = userRepository.findById(4);
//            comment.setUser(byId1.get());
//            comment.setDepth(99);
//            comment.setRef(10L);
//            comment.setStep(100);
//            commentRepository.save(comment);
//
//    }

}
