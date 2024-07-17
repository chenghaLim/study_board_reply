package com.study.board.security.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class JwtToken {
    private String accessToken;
    private String refreshToken;
}
