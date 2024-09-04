package org.trainee.stockservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "stock")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Stock {
    @Id
    @SequenceGenerator(name = "stock_seq",
            sequenceName = "stock_sequence",
            allocationSize = 25
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stock_seq")
    private Long id;

    @Column(nullable = false)
    private String name;
}
