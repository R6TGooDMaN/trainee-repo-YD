package org.trainee.userservice.mapper;

import lombok.experimental.UtilityClass;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.trainee.userservice.dto.UserRequest;
import org.trainee.userservice.dto.UserResponseDto;
import org.trainee.userservice.model.User;

import java.util.Collections;

@UtilityClass
public class UserMapper {

    private PasswordEncoder passwordEncoder;


    public UserResponseDto toUserResponseDTO(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(user.getId());
        userResponseDto.setUsername(user.getUsername());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setPhone(user.getPhone());
        userResponseDto.setRole(user.getRoles());
        return userResponseDto;

    }
    public User buildUser(UserRequest userRequest){
        return User.builder()
                .username(userRequest.getUsername())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .email(userRequest.getEmail())
                .phone(userRequest.getPhone())
                .roles(userRequest.getRole())
                .build();
    }
    public static UserRepresentation getUserRepresentation(UserRequest userRequest) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(userRequest.getUsername());
        userRepresentation.setEmail(userRequest.getEmail());
        userRepresentation.setEnabled(true);

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(userRequest.getPassword());
        userRepresentation.setCredentials(Collections.singletonList(credentialRepresentation));
        return userRepresentation;
    }
}
