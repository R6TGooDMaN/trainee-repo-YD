package org.trainee.userservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.trainee.userservice.dto.UserRegistrationDto;
import org.trainee.userservice.model.User;
import org.trainee.userservice.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUser() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User userCreated = userService.createUser(user);
        return ResponseEntity.ok(userCreated);
    }

    @PostMapping("/registration")
    public ResponseEntity<User> registerUser(@RequestBody UserRegistrationDto userRegistrationDto) {
        User user = User.builder()
                .username(userRegistrationDto.getUsername())
                .phone(userRegistrationDto.getPhone())
                .password(userRegistrationDto.getPassword())
                .email(userRegistrationDto.getEmail())
                .roles(userRegistrationDto.getRole())
                .build();
        User registeredUser = userService.registerUser(user);
        return ResponseEntity.ok(registeredUser);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }



}
