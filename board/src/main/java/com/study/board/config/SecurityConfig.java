package com.study.board.config;


import com.study.board.exception.UnauthorizedException;
import com.study.board.security.auth.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig implements WebMvcConfigurer {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, JwtAuthorizationFilter jwtAuthorizationFilter) throws Exception {

        // api 서버로 사용하기 때문에 csrf 해제 (jwt로 대체)
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        // 로그인 인증창이 뜨지 않게 비활성화
        httpSecurity.httpBasic(AbstractHttpConfigurer::disable);

        // form 로그인 해제
        httpSecurity.formLogin(AbstractHttpConfigurer::disable);

        // jSessionId 사용 거부
        httpSecurity.sessionManagement(config -> config
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        httpSecurity.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
        // 인증, 권한 필터 설정
        httpSecurity.authorizeHttpRequests(config -> config
                .requestMatchers("/favicon.ico", "classpath:/static/**", "classpath:/templates/**").permitAll()
                .requestMatchers("/", "/boards", "/boards/showOne/*").permitAll()
                .requestMatchers("/users/sign*", "/api/v1/auth/sign*").permitAll()
                .requestMatchers("/comments/*").permitAll()
                .requestMatchers("/boards/new").hasRole("USER")
                .anyRequest().authenticated());

        // 401 Error 처리, Authorization 즉, 인증과정에서 실패할 시 처리
        // 403 Error 처리, 인증과는 별개로 추가적인 권한이 충족되지 않는 경우
//        httpSecurity.exceptionHandling(exceptionHandling ->
//                exceptionHandling
//                        .authenticationEntryPoint((request, response, authException) -> {
//                            throw new UnauthorizedException("인증이 필요합니다.", false);
//                        })
//                        .accessDeniedHandler((request, response, accessDeniedException) -> {
//                            throw new UnauthorizedException("권한이 부족합니다.", true);
//                        }));
//        httpSecurity.exceptionHandling(exceptionHandling ->
//                exceptionHandling
//                        .authenticationEntryPoint((request, response, authException) -> {
//                            response.sendRedirect("/");
//                        })
//                        .accessDeniedHandler((request, response, accessDeniedException) -> {
//                            response.sendRedirect("/");
//                        }));

        return httpSecurity.getOrBuild();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        // 암호화 방식을 BCrypt 로 지정
        return new BCryptPasswordEncoder();
    }
}
