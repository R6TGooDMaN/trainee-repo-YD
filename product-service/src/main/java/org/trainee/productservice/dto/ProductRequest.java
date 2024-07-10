package org.trainee.productservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class ProductRequest {

    @NotBlank
    @Size(min = 1, max = 300)
    private String name;

    @NotBlank
    @Size(min = 1, max = 600)
    private String description;

    @NotNull
    @Positive
    private Integer price;
}
