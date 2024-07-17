package com.study.board.config;


import com.study.board.exception.CommonExceptionHandler;
import com.study.board.exception.UnauthorizedException;
import com.study.board.security.auth.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
@EnableScheduling
public class SecurityConfig implements WebMvcConfigurer {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, JwtAuthorizationFilter jwtAuthorizationFilter) throws Exception {

        // api 서버로 사용하기 때문에 csrf 해제 (jwt로 대체)
        httpSecurity.csrf(config -> config.disable());

        // 로그인 인증창이 뜨지 않게 비활성화
        httpSecurity.httpBasic(config -> config.disable());

        // form 로그인 해제
        httpSecurity.formLogin(config -> config.disable());

        // jSessionId 사용 거부
        httpSecurity.sessionManagement(config -> config
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        httpSecurity.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
        // 인증, 권한 필터 설정
        httpSecurity.authorizeHttpRequests(config -> config
                .requestMatchers("/api/v1/auth/signUp", "/api/v1/auth/signIn").permitAll()
                .requestMatchers("/static/css/app.css", "static/app.js", "/users/**", "/", "/boards", "assert.html", "detail.html", "/boards/showOne/*").permitAll()
                .requestMatchers("/api/v1/auth/logout").hasAnyRole("ADMIN", "USER")
                .anyRequest().authenticated());
        return httpSecurity.getOrBuild();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        // 암호화 방식을 BCrypt로 지정
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // h2-console 사용 및 resources 접근 허용 설정
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
}
