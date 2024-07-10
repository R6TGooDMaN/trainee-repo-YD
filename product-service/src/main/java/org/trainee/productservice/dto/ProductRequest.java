package org.trainee.productservice.dto;

import jakarta.validation.Valid;
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
    @Valid
    private String name;
    @Valid
    private String description;
    @Valid
    private Integer price;
}
