package org.trainee.userservice.mapper;

import lombok.experimental.UtilityClass;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.trainee.userservice.dto.UserRequest;
import org.trainee.userservice.dto.UserResponseDto;
import org.trainee.userservice.model.User;

@UtilityClass
public class UserMapper {

    private PasswordEncoder passwordEncoder;


    public static UserResponseDto toUserResponseDTO(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(user.getId());
        userResponseDto.setUsername(user.getUsername());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setPhone(user.getPhone());
        userResponseDto.setRole(user.getRoles());
        return userResponseDto;

    }
    public static User buildUser(UserRequest userRequest){
        return User.builder()
                .username(userRequest.getUsername())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .email(userRequest.getEmail())
                .phone(userRequest.getPhone())
                .roles(userRequest.getRole())
                .build();
    }
}
