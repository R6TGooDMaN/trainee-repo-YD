package org.trainee.productservice.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.trainee.productservice.dto.ProductRequest;
import org.trainee.productservice.dto.ProductResponse;
import org.trainee.productservice.mapper.ProductMapper;
import org.trainee.productservice.model.Product;
import org.trainee.productservice.repository.ProductRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductMapper productMapper;
    @InjectMocks
    private ProductService productService;
    @Mock
    private Product product;
    @Mock
    private ProductRequest productRequest;
    @Mock
    private ProductResponse productResponse;

    @BeforeEach
    public void setUp() {
        product = Product.builder()
                .id(1L)
                .name("Product Name")
                .description("Product Desc")
                .price(100)
                .build();
        productRequest = ProductRequest.builder()
                .name("Product Name")
                .description("Product Desc")
                .price(100)
                .build();
        productResponse = ProductResponse.builder()
                .id(1L)
                .name("Product Name")
                .description("Product Desc")
                .price(100)
                .build();
    }

    @Test
    public void ProductService_CreateProductTest() {
        when(productMapper.mapToProduct(productRequest)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.mapToProductResponse(product)).thenReturn(productResponse);

        ProductResponse resultResponse = productService.createProduct(productRequest);

        assertNotNull(resultResponse);
        assertEquals(productResponse, resultResponse);
    }

    @Test
    public void ProductService_FindProductTest() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.mapToProductResponse(product)).thenReturn(productResponse);

        ProductResponse resultResponse = productService.findProduct(1L);

        assertNotNull(resultResponse);
        assertEquals(productResponse, resultResponse);
    }

    @Test
    public void ProductService_UpdateProductTest() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.updateProductFromRequest(product, productRequest)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.mapToProductResponse(product)).thenReturn(productResponse);

        ProductResponse result = productService.updateProduct(1L, productRequest);

        assertNotNull(result);
        assertEquals(productResponse, result);
    }

    @Test
    public void ProductService_DeleteProductTest() {
        productService.deleteProduct(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    public void ProductService_CreateProductNegativeTest() {
        when(productMapper.mapToProduct(productRequest)).thenReturn(product);
        doThrow(DataAccessException.class).when(productRepository.save(product));

        assertThrows(DataAccessException.class, () -> productService.createProduct(productRequest));

    }
}
