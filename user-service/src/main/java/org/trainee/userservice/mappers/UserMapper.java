package org.trainee.userservice.mappers;

import lombok.experimental.UtilityClass;
import org.trainee.userservice.dto.UserRequest;
import org.trainee.userservice.dto.UserResponse;
import org.trainee.userservice.model.User;

@UtilityClass
public class UserMapper {
    public UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .phone(user.getPhone())
                .email(user.getEmail())
                .build();
    }

    public User mapToUser(UserRequest userRequest) {
        return User.builder()
                .username(userRequest.getUsername())
                .password(userRequest.getPassword())
                .phone(userRequest.getPhone())
                .email(userRequest.getEmail())
                .build();
    }
}
