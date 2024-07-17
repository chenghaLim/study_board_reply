package com.study.board.exception;

// 잘못된 요청 시 던질 exception
public class BadRequestException extends RuntimeException{
    public BadRequestException(String message) {
        super(message);
    }
}
