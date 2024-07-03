package org.trainee.productservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.trainee.productservice.model.Product;

public interface ProductRepository extends CrudRepository<Product, String> {

}
