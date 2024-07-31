package org.trainee.userservice.dto;

import lombok.Data;
import org.trainee.userservice.enums.Roles;

@Data
public class UserRegistrationDto {
    private String username;
    private String password;
    private String phone;
    private String email;
    private Roles role;
}
