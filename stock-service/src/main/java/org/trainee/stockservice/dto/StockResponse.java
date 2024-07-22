package org.trainee.stockservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StockResponse {

    private Long id;
    private String name;
    private List<StockProductResponse> stockProductResponses;
}
