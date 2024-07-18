package org.trainee.productservice.repository;

import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.trainee.productservice.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}