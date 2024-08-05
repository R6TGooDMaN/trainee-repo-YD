package org.trainee.userservice.service;

import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.trainee.productservice.enums.EntityType;
import org.trainee.productservice.exception.EntityNotFoundException;
import org.trainee.userservice.dto.UserRequest;
import org.trainee.userservice.dto.UserResponseDto;
import org.trainee.userservice.mapper.UserMapper;
import org.trainee.userservice.model.User;
import org.trainee.userservice.repository.UserRepository;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.trainee.userservice.mapper.UserMapper.toUserResponseDTO;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Keycloak keycloak;
    private final String realm;
    private final String ERROR_MESSAGE = "Failed to create user with name:{0} and email:{1}";
    private static final String USER_NOT_FOUND_MESSAGE = "Entity with name: {0} with ID: {1} not found";

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, Keycloak keycloak, @Value("${keycloak.realm}") String realm) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.keycloak = keycloak;
        this.realm = realm;
    }

    public UserResponseDto createUser(UserRequest userRequest) {
        User user = User.builder()
                .username(userRequest.getUsername())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .email(userRequest.getEmail())
                .phone(userRequest.getPhone())
                .roles(userRequest.getRole())
                .build();
        userRepository.save(user);

        RealmResource resource = keycloak.realm(realm);
        UserRepresentation userRepresentation = getUserRepresentation(userRequest);
        String message = MessageFormat.format(ERROR_MESSAGE, userRepresentation.getUsername(), userRepresentation.getEmail());
        Response response = resource.users().create(userRepresentation);
        if (response.getStatus() == 201) {
            return toUserResponseDTO(user);
        } else {
            throw new RuntimeException(message);
        }
    }

    public UserResponseDto getUserById(Long id) {
        String message = MessageFormat.format(USER_NOT_FOUND_MESSAGE, EntityType.USER.name(), id);
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(message));
        return toUserResponseDTO(user);
    }

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream().map(UserMapper::toUserResponseDTO).collect(Collectors.toList());
    }

    public void deleteUserById(Long id) {
        String message = MessageFormat.format(USER_NOT_FOUND_MESSAGE, EntityType.USER.name(), id);
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(message));
        userRepository.delete(user);
        RealmResource realmResource = keycloak.realm(realm);
        realmResource.users().delete(user.getUsername());
    }

    private static UserRepresentation getUserRepresentation(UserRequest userRequest) {
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
