package org.trainee.productservice.service;

import org.springframework.stereotype.Service;
import org.trainee.productservice.dto.ProductRequest;
import org.trainee.productservice.dto.ProductResponse;
import org.trainee.productservice.enums.EntityType;
import org.trainee.productservice.exception.EntityNotFoundException;
import org.trainee.productservice.mapper.ProductMapper;
import org.trainee.productservice.model.Product;
import org.trainee.productservice.repository.ProductRepository;

import java.text.MessageFormat;
import java.util.List;

@Service
public class ProductService {


    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private static final String PRODUCT_NOT_FOUND_MESSAGE = "Entity with name: {0} with ID: {1} not found";

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = productMapper.mapToProduct(productRequest);
        Product newProduct = productRepository.save(product);
        return productMapper.mapToProductResponse(newProduct);
    }

    public ProductResponse findProduct(Long id) {
        String message = MessageFormat.format(PRODUCT_NOT_FOUND_MESSAGE, EntityType.PRODUCT.name(), id);
        Product product = productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(message));
        return productMapper.mapToProductResponse(product);
    }

    public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
        String message = MessageFormat.format(PRODUCT_NOT_FOUND_MESSAGE, EntityType.PRODUCT.name(), id);
        return productRepository.findById(id)
                .map(existingProduct -> {
                    Product updatedProduct = productMapper.updateProductFromRequest(existingProduct, productRequest);
                    Product savedProduct = productRepository.save(updatedProduct);
                    return productMapper.mapToProductResponse(savedProduct);
                })
                .orElseThrow(() -> new EntityNotFoundException(message));
    }

    public void deleteProduct(Long id) {
        String message = MessageFormat.format(PRODUCT_NOT_FOUND_MESSAGE, EntityType.PRODUCT.name(), id);
        productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(message));
        productRepository.deleteById(id);
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(productMapper::mapToProductResponse)
                .toList();
    }
}