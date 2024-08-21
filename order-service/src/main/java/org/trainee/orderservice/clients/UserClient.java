package org.trainee.orderservice.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.trainee.orderservice.dto.UserDto;
import org.trainee.orderservice.exception.NoUserException;

import java.text.MessageFormat;

@Component
public class UserClient {
    private final RestTemplate restTemplate;
    private final String userUrl = "/api/v1/users/";
    private final String serviceUrl;
    private final String NO_USER_MESSAGE = "There is no user with id: {0}";

    public UserClient(RestTemplate restTemplate, @Value("${user-service.url}") String serviceUrl) {
        this.restTemplate = restTemplate;
        this.serviceUrl = serviceUrl;
    }

    public UserDto getUser(Long id, String token) {
        String userMessage = MessageFormat.format(NO_USER_MESSAGE, id);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<UserDto> response = restTemplate.exchange(serviceUrl + userUrl + id, HttpMethod.GET, entity, UserDto.class);
            return response.getBody();
        } catch (HttpServerErrorException e) {
            throw new NoUserException(userMessage);
        }
    }
}
