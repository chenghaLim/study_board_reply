package com.study.board.service;

import com.study.board.dto.UserDTO;
import com.study.board.entity.User;
import com.study.board.exception.BadRequestException;
import com.study.board.exception.ResDTO;
import com.study.board.repository.UserRepository;
import com.study.board.security.auth.JwtProvider;
import com.study.board.security.auth.JwtToken;
import com.study.board.security.auth.JwtTokenType;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public UserDTO.Response findById(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("유저를 찾을 수 없습니다."));
        return convertToDto(user);
    }

    @Transactional
    public HttpEntity<?> join(@Valid UserDTO.Request userDTO) {
        int code = 0;
        String message = "회원 가입 성공";
        Optional<User> byEmail = userRepository.findByEmail(userDTO.getEmail());
        Optional<User> byName = userRepository.findByName(userDTO.getName());
        if (byEmail.isPresent() || byName.isPresent()) {
            code = 1;
            message = "이미 사용 중 입니다.";
        }
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userRepository.save(userDTO.toEntity());
        return new ResponseEntity<>(
                ResDTO.builder()
                        .code(code)
                        .message(message)
                        .build(),
                HttpStatus.OK
        );
    }

    @Transactional
    public HttpEntity<?> login(UserDTO.logInRequest userDTO) {
        Optional<User> userOptional = userRepository.findByEmail(userDTO.getEmail());

        if (userOptional.isEmpty()) {
            throw new BadRequestException("존재하지 않는 유저입니다.");
        }

        User user = userOptional.get();

        if (!passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            throw new BadRequestException("패스워드가 일치하지 않습니다.");
        }

        String accessToken = jwtProvider.createToken(user, JwtTokenType.ACCESS_TOKEN);
        String refreshToken = jwtProvider.createToken(user, JwtTokenType.REFRESH_TOKEN);

        return new ResponseEntity<>(
                ResDTO.builder()
                        .code(0)
                        .message("로그인에 성공하였습니다.")
                        .data(JwtToken.builder().accessToken(accessToken)
                                .refreshToken(refreshToken).build())
                        .build(),
                HttpStatus.OK);
    }


    /* Entity -> dto */
    private UserDTO.Response convertToDto(User user) {
        UserDTO.Response userDTO = new UserDTO.Response();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setName(user.getName());
        userDTO.setRole(user.getRole());
        userDTO.setUpdatedAt(user.getUpdatedAt());
        return userDTO;
    }
}
