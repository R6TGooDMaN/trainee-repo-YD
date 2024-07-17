package org.trainee.productservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 300, message = "Name cannot exceed 300 characters")
    private String name;

    @NotBlank(message = "Description cannot be blank")
    @Size(max = 600, message = "Description cannot exceed 600 characters")
    private String description;

    @Min(value = 1, message = "Price must be greater than zero")
    private Integer price;
}
