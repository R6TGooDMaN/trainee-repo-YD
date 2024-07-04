package org.trainee.productservice.mapper;

import org.springframework.stereotype.Component;
import org.trainee.productservice.dto.ProductRequest;
import org.trainee.productservice.dto.ProductResponse;
import org.trainee.productservice.model.Product;


@Component
public class ProductMapper {
    public ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }

    public Product mapToProduct(ProductRequest productRequest) {
        return Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();
    }

    public Product updateProductFromRequest(Product product, ProductRequest productRequest) {
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        return product;
    }

}