package org.trainee.userservice.service;

import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.trainee.productservice.enums.EntityType;
import org.trainee.productservice.exception.EntityNotFoundException;
import org.trainee.userservice.dto.UserRequest;
import org.trainee.userservice.dto.UserResponseDto;
import org.trainee.userservice.mapper.UserMapper;
import org.trainee.userservice.model.User;
import org.trainee.userservice.repository.UserRepository;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.trainee.userservice.mapper.UserMapper.getUserRepresentation;


@Service
public class UserService {

    private final JdbcTemplate jdbcTemplate;
    private final UserRepository userRepository;
    private final Keycloak keycloak;
    private final String realm;
    private final String ERROR_MESSAGE = "Failed to create user with name:{0} and email:{1}";
    private static final String USER_NOT_FOUND_MESSAGE = "Entity with name: {0} with ID: {1} not found";

    public UserService(JdbcTemplate jdbcTemplate, UserRepository userRepository, Keycloak keycloak, @Value("${keycloak.realm}") String realm) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRepository = userRepository;
        this.keycloak = keycloak;
        this.realm = realm;
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

    public List<Map<String,Object>> getUsers() {
        String sqlQuery = "SELECT * FROM users";
        return jdbcTemplate.queryForList(sqlQuery);
    }

    public UserResponseDto getUserById(Long id) {
        String message = MessageFormat.format(USER_NOT_FOUND_MESSAGE, EntityType.USER.name(), id);
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(message));
        return UserMapper.toUserResponseDTO(user);
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


}
