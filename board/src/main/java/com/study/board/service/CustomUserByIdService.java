package com.study.board.service;

import com.study.board.dto.UserDTO;
import com.study.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserByIdService {
    private final UserRepository userRepository;

    public UserDTO.Response findUserDTOById(int id) {
        return userRepository.findById(id)
                .map(user -> new UserDTO.Response(user.getId(), user.getEmail(), user.getPassword(), user.getName(), user.getRole()))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

}
