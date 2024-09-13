package org.trainee.orderservice.dto;

import lombok.Data;
import org.trainee.orderservice.enums.OrderStatuses;

import java.time.LocalDate;

@Data
public class FiltersDto {
    private OrderStatuses status;
    private LocalDate date;
    private String sortBy;
}
