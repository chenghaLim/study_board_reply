package com.study.board.exception;

// 인증 exception
public class UnauthorizedException  extends RuntimeException{
    public UnauthorizedException(String message) {
        super(message);
    }
}
