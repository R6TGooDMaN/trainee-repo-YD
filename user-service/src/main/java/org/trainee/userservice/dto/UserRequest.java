package org.trainee.userservice.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserRequest {
    private String username;
    private String password;
    private String email;
    private String phone;
}
