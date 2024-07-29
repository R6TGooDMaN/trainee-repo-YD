package org.trainee.stockservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockResponse {

    private Long id;
    private String name;
    private List<StockProductResponse> stockProductResponses;
}
