package com.appcenter.wnt.service;

import com.appcenter.wnt.domain.User;
import com.appcenter.wnt.dto.request.CreateUserRequest;
import com.appcenter.wnt.dto.response.UserResponse;
import com.appcenter.wnt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserResponse getUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("유저가 존재하지 않습니다."));
        return UserResponse.from(user);
    }

    public UserResponse register(CreateUserRequest userRequest) {
        User user = User.of(userRequest.nickname());
        userRepository.save(user);
        return UserResponse.from(user);
    }

    public void deleteUser(Long userId){
        User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("유저가 존재하지 않습니다."));
        userRepository.delete(user);
    }

}
