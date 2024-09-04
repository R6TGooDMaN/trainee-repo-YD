package org.trainee.stockservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.trainee.stockservice.key.StockProductKey;

@Entity
@Table(name = "stock_product")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@IdClass(StockProductKey.class)
public class StockProduct {
    @Id
    private Long stockId;
    @Id
    private Long productId;
    @Max(value = 400)
    private Integer quantity;


}
