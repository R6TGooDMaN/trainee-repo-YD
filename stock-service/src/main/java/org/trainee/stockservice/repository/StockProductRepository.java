package org.trainee.stockservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.trainee.stockservice.key.StockProductKey;
import org.trainee.stockservice.model.StockProduct;

@Repository
public interface StockProductRepository extends JpaRepository<StockProduct, StockProductKey> {
}
