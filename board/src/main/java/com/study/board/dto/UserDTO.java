package com.study.board.dto;

import com.study.board.entity.Role;
import com.study.board.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.io.Serializable;

public class UserDTO {
    /**
     * 회원 Service 요청(Request) DTO 클래스
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {

        private int id;

        @Pattern(regexp = "^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$", message = "이메일 형식이 올바르지 않습니다.")
        @NotBlank(message = "이메일이 정확하지 않습니다")
        @Email
        private String email;

        @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
        @NotBlank(message = "패스워드가 정확하지 않습니다.")
        private String password;

        @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9-_]{2,10}$", message = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.")
        @NotBlank(message = "닉네임은 필수 입력 값입니다.")
        private String name;

        private Role role;

        /* DTO -> Entity */
        public User toEntity() {
            User user = User.builder()
                    .email(email)
                    .password(password)
                    .name(name)
                    .role(role.USER)
                    .build();
            return user;
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class logInRequest {

        @Pattern(regexp = "^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$", message = "이메일 형식이 올바르지 않습니다.")
        @NotBlank(message = "이메일을 입력하세요")
        @Email
        private String email;

        @NotBlank(message = "패스워드를 입력하세요.")
        private String password;

    }

    @Getter
    @Setter
    public static class Response implements Serializable {

        private int id;
        private String email;
        private String name;
        private Role role;
        private String updatedAt;

    }
}
