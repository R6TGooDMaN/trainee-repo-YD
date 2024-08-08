package org.trainee.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.trainee.userservice.enums.Roles;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private Long id;
    private String username;
    private String password;
    private String phone;
    private String email;
    private Roles role;
}
