package org.trainee.stockservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.trainee.productservice.model.Product;
import org.trainee.stockservice.key.StockProductKey;

@Entity
@Table(name = "stock_product")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class StockProduct {

    @EmbeddedId
    private StockProductKey id;

    @ManyToOne
    @MapsId("stockId")
    @JoinColumn(name = "stock_id")
    private Stock stock;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(length = 400)
    private Integer quantity;

}
