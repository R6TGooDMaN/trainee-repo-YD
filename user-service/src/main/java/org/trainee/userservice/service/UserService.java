package org.trainee.userservice.service;

import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.trainee.productservice.exception.EntityNotFoundException;
import org.trainee.userservice.dto.UserRequest;
import org.trainee.userservice.dto.UserResponse;
import org.trainee.userservice.enums.Roles;
import org.trainee.userservice.mappers.UserMapper;
import org.trainee.userservice.model.User;
import org.trainee.userservice.repository.UserRepository;

import java.util.List;
import java.util.Set;

@Service
public class UserService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(UserMapper::mapToUserResponse).toList();
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
        return UserMapper.mapToUserResponse(user);
    }

    @Transactional
    public User registerUser(UserRequest userRequest) {
        userRepository.findByUsername(userRequest.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        User user = User.builder()
                .username(userRequest.getUsername())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .email(userRequest.getEmail())
                .phone(userRequest.getPhone())
                .roles(Set.of(Roles.USER))
                .build();
        return userRepository.save(user);
    }
}
