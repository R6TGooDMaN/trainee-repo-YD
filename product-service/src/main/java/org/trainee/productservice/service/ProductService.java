package org.trainee.productservice.service;

import org.springframework.stereotype.Service;
import org.trainee.productservice.dto.ProductRequest;
import org.trainee.productservice.dto.ProductResponse;
import org.trainee.productservice.exception.ProductNotFoundException;
import org.trainee.productservice.mapper.ProductMapper;
import org.trainee.productservice.model.Product;
import org.trainee.productservice.repository.ProductRepository;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public ProductResponse createProduct(ProductRequest productRequest){
        Product product = productMapper.mapToProduct(productRequest);
        Product newProduct = productRepository.save(product);
        return productMapper.mapToProductResponse(newProduct);
    }

    public ProductResponse findProduct(Long id){
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found!"));
        return productMapper.mapToProductResponse(product);
    }

    public ProductResponse updateProduct(Long id, ProductRequest productRequest){
        return productRepository.findById(id)
                .map(existingProduct -> {
                    existingProduct.setName(productRequest.getName());
                    existingProduct.setDescription(productRequest.getDescription());
                    existingProduct.setPrice(productRequest.getPrice());
                    Product newProduct = productRepository.save(existingProduct);
                    return productMapper.mapToProductResponse(newProduct);
                })
                .orElseThrow(() -> new ProductNotFoundException("Product not found!"));
    }

    public boolean deleteProduct(Long id){
        return productRepository.findById(id)
                .map(product -> {
                    productRepository.delete(product);
                    return true;
                })
                .orElseThrow(() -> new ProductNotFoundException("Product not found!"));
    }

    public List<ProductResponse> getAllProducts(){
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(productMapper::mapToProductResponse)
                .toList();
    }
}