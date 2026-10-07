package com.bhavesh.orbit.userService.mapper;

import com.bhavesh.orbit.userService.dto.SignupRequestDto;
import com.bhavesh.orbit.userService.dto.UserDto;
import com.bhavesh.orbit.userService.entity.User;
import org.springframework.stereotype.Component;

/** Explicit conversions between user entities and API DTOs. */
@Component
public class UserMapper {

    public User toEntity(SignupRequestDto request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        return user;
    }

    public UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        return dto;
    }
}
