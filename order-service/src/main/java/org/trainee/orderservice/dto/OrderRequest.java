package org.trainee.orderservice.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;
import org.trainee.orderservice.enums.OrderStatuses;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Validated
public class OrderRequest {
    @NotNull
    private Long userId;
    @NotNull
    @Size(min = 1, max = 50)
    private Long orderNumber;
    @NotNull
    private LocalDate orderDate;
    @NotNull
    private OrderStatuses orderStatus;
}
