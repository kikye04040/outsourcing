package com.sparta.outsourcing.domain.user.service;

import com.sparta.outsourcing.domain.user.dto.response.UserResponse;
import com.sparta.outsourcing.domain.user.entity.User;
import com.sparta.outsourcing.domain.user.exception.UserNotFoundException;
import com.sparta.outsourcing.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserResponse findByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        return UserResponse.from(user);
    }
}
