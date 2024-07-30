package org.trainee.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

    @NotNull
    public Long id;
    @NotBlank
    @Size(min = 10, max = 50)
    public String username;
    @NotBlank
    @Size(min = 10, max = 10)
    public String password;
    @NotBlank
    @Size(min = 13, max = 13)
    public String phone;
    @NotBlank
    @Size(min = 5, max = 60)
    public String email;

}
