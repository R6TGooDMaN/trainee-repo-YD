package org.trainee.productservice.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.trainee.productservice.dto.ProductRequest;
import org.trainee.productservice.dto.ProductResponse;
import org.trainee.productservice.mapper.ProductMapper;
import org.trainee.productservice.repository.ProductRepository;
import org.trainee.productservice.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
@Validated
public class ProductController {
    private final ProductService productService;
    public ProductController(ProductService productService, ProductRepository productRepository) {
        this.productService = productService;
    }



    @PostMapping("/save")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest productRequest) {
        ProductResponse productResponse = productService.createProduct(productRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(productResponse);

    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findProduct(@PathVariable Long id) {
        ProductResponse productResponse = productService.findProduct(id);
        return ResponseEntity.ok(productResponse);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id,
                                                         @Valid @RequestBody ProductRequest productRequest) {
        ProductResponse productResponse = productService.updateProduct(id, productRequest);
        return ResponseEntity.ok(productResponse);
    }

    @Valid
    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

}
