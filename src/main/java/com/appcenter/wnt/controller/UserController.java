package com.appcenter.wnt.controller;

import com.appcenter.wnt.dto.request.UserRequest;
import com.appcenter.wnt.dto.response.UserResponse;
import com.appcenter.wnt.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "User",description = "User API")
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> register(@RequestBody UserRequest userRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(userRequest));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> delete(@PathVariable("userId") Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body("유저 회원탈퇴 성공");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getAllUsers(@PathVariable("userId") Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUser(userId));
    }
}
