package org.trainee.userservice.service;

import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.trainee.userservice.clients.ProductClient;
import org.trainee.userservice.dto.UserRequest;
import org.trainee.userservice.dto.UserResponseDto;
import org.trainee.userservice.exception.UserNotFoundException;
import org.trainee.userservice.mapper.UserMapper;
import org.trainee.userservice.model.User;
import org.trainee.userservice.repository.UserRepository;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

import static org.trainee.userservice.mapper.UserMapper.getUserRepresentation;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final Keycloak keycloak;
    private final String realm;
    private final String ERROR_MESSAGE = "Failed to create user with name:{0} and email:{1}";
    private static final String USER_NOT_FOUND_MESSAGE = "Entity with name: {0} with ID: {1} not found";
    private final ProductClient productClient;

    public UserService(UserRepository userRepository, Keycloak keycloak, @Value("${keycloak.realm}") String realm, ProductClient productClient) {
        this.userRepository = userRepository;
        this.keycloak = keycloak;
        this.realm = realm;
        this.productClient = productClient;
    }

    public UserResponseDto createUser(UserRequest userRequest) {
        User user = UserMapper.buildUser(userRequest);
        userRepository.save(user);
        RealmResource resource = keycloak.realm(realm);
        UserRepresentation userRepresentation = getUserRepresentation(userRequest);
        String message = MessageFormat.format(ERROR_MESSAGE, userRepresentation.getUsername(), userRepresentation.getEmail());
        Response response = resource.users().create(userRepresentation);
        if (response.getStatus() == 201) {
            return UserMapper.toUserResponseDTO(user);
        } else {
            throw new RuntimeException(message);
        }
    }

    public UserResponseDto getUserById(Long id) {
        String message = MessageFormat.format(USER_NOT_FOUND_MESSAGE, productClient.getUserType(), id);
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(message));
        return UserMapper.toUserResponseDTO(user);
    }

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream().map(UserMapper::toUserResponseDTO).collect(Collectors.toList());
    }

    public void deleteUserById(Long id) {
        String message = MessageFormat.format(USER_NOT_FOUND_MESSAGE, productClient.getUserType(), id);
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(message));
        userRepository.delete(user);
        RealmResource realmResource = keycloak.realm(realm);
        realmResource.users().delete(user.getUsername());
    }


}
