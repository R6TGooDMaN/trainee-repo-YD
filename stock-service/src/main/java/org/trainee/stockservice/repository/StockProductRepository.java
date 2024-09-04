package org.trainee.stockservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.trainee.stockservice.key.StockProductKey;
import org.trainee.stockservice.model.StockProduct;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockProductRepository extends JpaRepository<StockProduct, StockProductKey> {
    List<StockProduct> findByStockId(Long stockId);
    Optional<StockProduct> findByProductId(Long productId);
    Optional<StockProduct> findByProductIdAndStockId(Long productId, Long stockId);
    List<StockProduct> findAllByProductId(Long productId);
}
