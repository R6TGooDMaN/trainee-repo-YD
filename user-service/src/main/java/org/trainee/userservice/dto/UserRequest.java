package org.trainee.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.trainee.userservice.enums.Roles;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserRequest {
    private String username;
    private String password;
    private String email;
    private String phone;
    private Roles role;
}
