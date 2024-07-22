package org.trainee.stockservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.trainee.stockservice.model.Stock;

public interface StockRepository extends JpaRepository<Stock, Long> {

}
