package org.trainee.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.trainee.orderservice.enums.OrderStatuses;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    private Long Id;
    private Long userId;
    private Long orderNumber;
    private LocalDate orderDate;
    private OrderStatuses orderStatus;
}
