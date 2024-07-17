package com.study.board.exception;

// 인증 exception 401 error
public class UnauthorizedException  extends RuntimeException{
    private boolean isForbidden;

    public UnauthorizedException(String message, boolean isForbidden) {
        super(message);
        this.isForbidden = isForbidden;
    }

    public boolean isForbidden() {
        return isForbidden;
    }

    public UnauthorizedException(String message) {
        super(message);
    }
}
