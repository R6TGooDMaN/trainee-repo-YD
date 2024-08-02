package org.trainee.userservice.service;

import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.trainee.productservice.exception.EntityNotFoundException;
import org.trainee.userservice.dto.UserRequest;
import org.trainee.userservice.dto.UserResponseDto;
import org.trainee.userservice.model.User;
import org.trainee.userservice.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Keycloak keycloak;
    private final String realm;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, Keycloak keycloak, @Value("${keycloak.realm}") String realm) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.keycloak = keycloak;
        this.realm = realm;
    }

    public UserResponseDto createUser(UserRequest userRequest) {
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());
        user.setRoles(userRequest.getRole());
        userRepository.save(user);

        RealmResource resource = keycloak.realm(realm);
        UserRepresentation userRepresentation = getUserRepresentation(userRequest);

        Response response = resource.users().create(userRepresentation);
        if (response.getStatus() == 201) {
            return toUserResponseDTO(user);
        } else {
            throw new RuntimeException("Failed to create user!");
        }
    }

    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));
        return toUserResponseDTO(user);
    }

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream().map(this::toUserResponseDTO).collect(Collectors.toList());
    }

    public void deleteUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));
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

    private UserResponseDto toUserResponseDTO(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(user.getId());
        userResponseDto.setUsername(user.getUsername());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setPhone(user.getPhone());
        userResponseDto.setRole(user.getRoles());
        return userResponseDto;

    }
}
