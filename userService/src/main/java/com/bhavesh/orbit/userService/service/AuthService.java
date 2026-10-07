package com.bhavesh.orbit.userService.service;

import com.bhavesh.orbit.userService.dto.LoginRequestDto;
import com.bhavesh.orbit.userService.dto.SignupRequestDto;
import com.bhavesh.orbit.userService.dto.UserDto;
import com.bhavesh.orbit.userService.entity.User;
import com.bhavesh.orbit.userService.mapper.UserMapper;
import com.bhavesh.orbit.userService.exception.BadRequestException;
import com.bhavesh.orbit.userService.repository.UserRepository;
import com.bhavesh.orbit.userService.util.BCrypt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtService jwtService;


    public UserDto signUp(SignupRequestDto signupRequestDto) {

        log.info("SignUp a user with email: {} ", signupRequestDto.getEmail());

        boolean exists = userRepository.existsByEmail(signupRequestDto.getEmail());

        if (exists) {
            throw new BadRequestException("User already exists");
        }

        User user = userMapper.toEntity(signupRequestDto);
        user.setPassword(BCrypt.hash(signupRequestDto.getPassword()));
        user = userRepository.save(user);
        return userMapper.toDto(user);

    }

    public String login(LoginRequestDto loginRequestDto) {
        log.info("Login request for user with email: {}", loginRequestDto.getEmail());

        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(
                        () -> new BadRequestException("Invalid credentials"));

        boolean isPasswordMatch = BCrypt.match(loginRequestDto.getPassword(), user.getPassword());

        if (!isPasswordMatch) {
            throw new BadRequestException("Invalid credentials");
        }

        return jwtService.generateAccessToken(user);
    }


}
