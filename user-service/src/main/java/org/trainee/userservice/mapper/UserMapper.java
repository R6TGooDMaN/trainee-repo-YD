package org.trainee.userservice.mapper;

import lombok.experimental.UtilityClass;
import org.trainee.userservice.dto.UserResponseDto;
import org.trainee.userservice.model.User;

@UtilityClass
public class UserMapper {
    public static UserResponseDto toUserResponseDTO(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(user.getId());
        userResponseDto.setUsername(user.getUsername());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setPhone(user.getPhone());
        userResponseDto.setRole(user.getRoles());
        return userResponseDto;

    }
}
