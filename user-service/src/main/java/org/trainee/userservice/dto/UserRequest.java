package org.trainee.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @NotBlank
    @Size(min = 10, max = 50)
    private String username;
    @NotBlank
    @Size(min = 10, max = 10)
    private String password;
    @NotBlank
    @Size(min = 10, max = 50)
    private String email;
    @NotBlank
    @Size(min = 13, max = 13)
    private String phone;
    @NotNull
    private Roles role;
}
