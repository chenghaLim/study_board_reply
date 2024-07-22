package com.study.board.security.auth;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.board.exception.UnauthorizedException;
import com.study.board.repository.UserRepository;
import com.study.board.security.CustomUserDetails;
import com.study.board.service.CustomUserByIdService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;


@Component
@Slf4j
public class JwtAuthorizationFilter extends GenericFilterBean {
    // id를 이용해 CustomUserDetails를 가져오는 서비스
    private final CustomUserByIdService customUserByIdService;

    public JwtAuthorizationFilter(CustomUserByIdService customUserByIdService) {
        this.customUserByIdService = customUserByIdService;
    }

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain
    ) throws IOException, ServletException {
        // HttpServletRequest 객체로 서블릿 요청 객체를 캐스팅
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;

        // HttpServletResponse 객체로 서블릿 응답 객체를 캐스팅
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        // HTTP 요청의 헤더에서 JWT 토큰을 가져온다.
        // JwtProvider.HEADER는 토큰을 저장하는 데 사용되는 HTTP 헤더의 이름
        String prefixJwt = httpRequest.getHeader(JwtProvider.HEADER);

        // 토큰이 없으면 다음 필터로 넘긴다.
        if (prefixJwt == null || !prefixJwt.startsWith(JwtProvider.TOKEN_PREFIX)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        // 토큰 접두사 "Bearer "을 제거한다.
        String jwt = prefixJwt.replace(JwtProvider.TOKEN_PREFIX, "").trim();
        try {
            // jwtProvider 객체의 verify 메서드를 사용해 토큰을 검증한다.
            JwtProvider jwtProvider = new JwtProvider();
            DecodedJWT decodedJWT = jwtProvider.verify(jwt);
            log.info("로그: {}", decodedJWT);

            // 토큰에서 id를 가져온다. (우리가 id로 설정 해놓은 주제, 즉 subject)
            int id = Integer.parseInt(decodedJWT.getSubject());
            log.info("아이디: {}", id);

            // id를 이용해 CustomUserDetails를 가져온다.
            UserDetails userDetails = new CustomUserDetails(customUserByIdService.findUserDTOById(id));
            log.info("있냐? :{}", userDetails);

            // 가져온 CustomUserDetails를 사용해
            // Authentication에 담길 UsernamePasswordAuthenticationToken 객체를 생성한다.
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities());
            log.info("있음: {}", authentication);

            // SecurityContext에 Authentication 객체를 저장한다.
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("??????: {}",SecurityContextHolder.getContext());
        } catch (UnauthorizedException e) {
            // json 형태로 401 응답
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 상태 코드 설정
            httpResponse.setContentType("application/json;charset=UTF-8"); // 컨텐츠 타입 설정

            // JSON으로 오류 메시지 생성
            Map<String, Object> data = new HashMap<>();
            data.put("code", -1);
            data.put("message", "인증 오류: " + e.getMessage());
            data.put("data", "Unauthorized");
            data.put("status", HttpServletResponse.SC_UNAUTHORIZED);

            // ObjectMapper를 사용해 Map을 JSON으로 변환
            String json = new ObjectMapper().writeValueAsString(data);

            // 응답에 JSON 쓰기
            httpResponse.getWriter().write(json);
            httpResponse.getWriter().flush();
            httpResponse.getWriter().close();
            return;
        }finally {
            filterChain.doFilter(servletRequest, servletResponse);
        }

    }
}
