package org.trainee.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.trainee.productservice.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
