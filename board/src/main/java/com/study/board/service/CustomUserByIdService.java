package com.study.board.service;

import com.study.board.entity.User;
import com.study.board.repository.UserRepository;
import com.study.board.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserByIdService {
    private final UserRepository userRepository;

    public CustomUserDetails loadUserById(int id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found with id : " + id));
        return new CustomUserDetails(user);
    }
}
