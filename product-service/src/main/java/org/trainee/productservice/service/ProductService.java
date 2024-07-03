package org.trainee.productservice.service;

import org.springframework.stereotype.Service;
import org.trainee.productservice.dto.ProductRequest;
import org.trainee.productservice.dto.ProductResponse;
import org.trainee.productservice.mapper.ProductMapper;
import org.trainee.productservice.model.Product;
import org.trainee.productservice.repository.ProductRepository;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse createProduct(ProductRequest productRequest){
        Product product = ProductMapper.mapToProduct(productRequest);
        Product newProduct = productRepository.save(product);
        return ProductMapper.mapToProductResponse(newProduct);
    }

    public ProductResponse findProduct(Long id){
        return productRepository.findById(id)
                .map(ProductMapper::mapToProductResponse)
                .orElse(null);
    }

    public ProductResponse updateProduct(Long id, ProductRequest productRequest){
        return productRepository.findById(id)
                .map(existingProduct -> {
                    existingProduct.setName(productRequest.getName());
                    existingProduct.setDescription(productRequest.getDescription());
                    existingProduct.setPrice(productRequest.getPrice());
                    Product newProduct = productRepository.save(existingProduct);
                    return ProductMapper.mapToProductResponse(newProduct);
                })
                .orElse(null);
    }

    public boolean deleteProduct(Long id){
        return productRepository.findById(id)
                .map(product -> {
                    productRepository.delete(product);
                    return true;
                })
                .orElse(false);
    }

    public List<ProductResponse> getAllProducts(){
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(ProductMapper::mapToProductResponse)
                .toList();
    }
}
