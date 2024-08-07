package org.trainee.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    @Size(max = 50)
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    @Size(min = 10, max = 50)
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+[A-Za-z]{2,}$")
    private String email;
    @NotBlank
    @Size(min = 13, max = 13)
    @Pattern(regexp = "^\\+\\d{12}$")
    private String phone;
    @NotNull
    private Roles role;
}
