package com.sparta.outsourcing.domain.user.service;

import com.sparta.outsourcing.domain.user.dto.CustomUserDetails;
import com.sparta.outsourcing.domain.user.dto.request.WithdrawRequest;
import com.sparta.outsourcing.domain.user.dto.response.UserListResponse;
import com.sparta.outsourcing.domain.user.dto.response.UserResponse;
import com.sparta.outsourcing.domain.user.entity.User;
import com.sparta.outsourcing.domain.user.exception.InvalidPasswordException;
import com.sparta.outsourcing.domain.user.exception.UserNotFoundException;
import com.sparta.outsourcing.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserResponse findByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        return UserResponse.from(user);
    }

    public UserResponse findById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return UserResponse.from(user);
    }

    public UserListResponse findAll() {
        List<User> userList = userRepository.findAll();
        List<UserResponse> userResponseList = new ArrayList<>();
        for (User user : userList) {
            UserResponse userResponse = UserResponse.from(user);
            userResponseList.add(userResponse);
        }
        return UserListResponse.builder().userResponseList(userResponseList).build();
    }

    @Transactional
    public void withDraw(WithdrawRequest withdrawRequest, CustomUserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getEmail()).orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(withdrawRequest.getPassword(), user.getPassword())) {
            throw new InvalidPasswordException();
        }

        userRepository.delete(user);
    }
}
