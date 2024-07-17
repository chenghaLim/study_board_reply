package com.study.board.security.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.study.board.entity.User;
import com.study.board.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;


@Component
public class JwtProvider {

    // 엑세스 토큰 유효기간 1일 설정
    private static final int EXP_ACCESS = 1000 * 60 * 60 * 24;
    // 리프레시 토큰 유효기간 7일 설정
    private static final int EXP_REFRESH = 1000 * 60 * 60 * 24 * 7;
    // 토큰 prefix 설정
    public static final String TOKEN_PREFIX = "Bearer ";
    // 토큰이 담길 헤더
    public static final String HEADER = "Authorization";

    private String SECRET = "24fb2557fad0be76049e6677c3d7fcdb5ebe3cc4483f86751cfd7d4478a6ce6e";

    public String createToken(User user, JwtTokenType tokenType){
        int exp = tokenType.compareTo(JwtTokenType.ACCESS_TOKEN) == 0 ? EXP_ACCESS : EXP_REFRESH;

        return JWT.create()
                // 고유값
                .withSubject(Integer.toString(user.getId()))
                // 만료 시간 설정 (현재 시간 + 유효기간)
                .withExpiresAt(new Date(System.currentTimeMillis() +exp))
                .withClaim("role", user.getRole().getValue())
                .withClaim("token-type",tokenType.name())
                .sign(Algorithm.HMAC512(SECRET));
    }

    // 토큰 검증 함수
    // 토큰이 유효하면 DecodedJWT 객체를 반환하고, 유효하지 않으면 UnauthorizedException 발생
    public DecodedJWT verify(String jwt) throws UnauthorizedException {
        try {
            // 시크릿 키를 이용해 토큰을 검증한다.
            return JWT.require(Algorithm.HMAC512(SECRET))
                    .build().verify(jwt);
        } catch (Exception e) {
            // 검증 실패 시 예외 발생
            throw new UnauthorizedException("token 값이 잘못되었습니다. " + e.getMessage());
        }
    }

    public static int getUserIdFromToken(DecodedJWT jwt) {
        return Integer.parseInt(jwt.getSubject());
    }


    public Long getExpiration(String jwt) throws UnauthorizedException {
        try {
            DecodedJWT decodedJWT = verify(jwt);
            return decodedJWT.getExpiresAt().getTime() - System.currentTimeMillis();
        } catch (Exception e) {
            throw new UnauthorizedException("토큰 만료 시간 조회 중 오류가 발생했습니다. " + e.getMessage());
        }
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
