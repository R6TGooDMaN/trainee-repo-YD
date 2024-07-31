package org.trainee.userservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.trainee.productservice.exception.EntityNotFoundException;
import org.trainee.userservice.model.User;
import org.trainee.userservice.repository.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;

    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;
    @Value("${keycloak.realm}")
    private String realm;
    @Value("${keycloak.resource}")
    private String resource;
    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RestTemplate restTemplate) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.restTemplate = restTemplate;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("User with username " + username + " not found"));
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User registeredUser = userRepository.save(user);
        registerInKeycloak(registeredUser);
        return registeredUser;
    }

    public void deleteUserById(Long id) {
        userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));
        userRepository.deleteById(id);
    }

    private void registerInKeycloak(User registeredUser) {
        String url = String.format("%s/admin/realms/%s/users", authServerUrl, realm);
        Map<String, Object> body = new HashMap<>();
        body.put("username", registeredUser.getUsername());
        body.put("enabled", true);

        Map<String, String> creds = new HashMap<>();
        creds.put("type", "password");
        creds.put("value", registeredUser.getPassword());
        creds.put("temporary", "false");
        List<Map<String, String>> credentials = new ArrayList<>();
        credentials.add(creds);
        body.put("credentials", credentials);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + getAdminAccessToken());

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        if (response.getStatusCode() == HttpStatus.CREATED) {
            System.out.println("Successfully registered user");
        } else {
            System.out.println("Failed to register user");
        }
    }

    private String getAdminAccessToken() {
        String url = String.format("%s/realms/%s/protocol/openid-connect/token", authServerUrl, realm);

        HttpHeaders headers = new HttpHeaders();
        headers.set("content-type", "application/x-www-form-urlencoded");

        Map<String, String> body = new HashMap<>();
        body.put("client_id", resource);
        body.put("client_secret", clientSecret);
        body.put("grant_type", "client_credentials");
        ResponseEntity<Map> response = restTemplate.postForEntity(url, new HttpEntity<>(body, headers), Map.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to get admin access token");

        }
        return response.getBody().get("access_token").toString();
    }


}
