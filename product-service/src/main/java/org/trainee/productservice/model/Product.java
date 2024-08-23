package org.trainee.productservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "product")
public class Product {

    public Product(Long productId) {
        this.id = productId;
    }

    @Id
    @SequenceGenerator(name = "product_seq",
            sequenceName = "product_sequence",
            allocationSize = 25
    )
    @GeneratedValue(generator = "product_seq")
    private Long id;
    private String name;
    private String description;

    @Min(value = 1, message = "Price must be grated than zero")
    @Column(nullable = false)
    private Integer price;
}