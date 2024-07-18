package org.trainee.productservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.trainee.productservice.configuration.TestConfiguration;
import org.trainee.productservice.dto.ProductRequest;
import org.trainee.productservice.model.Product;
import org.trainee.productservice.repository.ProductRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProductControllerTest extends TestConfiguration {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;


    @Test
    public void ProductController_CreateProductTest() throws Exception {
        ProductRequest productRequest = ProductRequest.builder()
                .name("Test Product")
                .description("Test Description")
                .price(100)
                .build();

        ResultActions resultActions = mockMvc.perform(post("/api/v1/product/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)));

        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.price").value(100));
    }

    @Test
    public void ProductController_CreateProductNegativePriceTest() throws Exception {
        ProductRequest productRequest = ProductRequest.builder()
                .name("Test Product")
                .description("Test Description")
                .price(-100)
                .build();

        ResultActions resultActions = mockMvc.perform(post("/api/v1/product/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)));

        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void ProductController_FindProductTest() throws Exception {
        Product product = new Product();
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(100);
        Product savedProduct = productRepository.save(product);

        Long productId = savedProduct.getId();

        ResultActions resultActions = mockMvc.perform(get("/api/v1/product/" + productId)
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.price").value(100));
    }

    @Test
    public void ProductController_FindNotExistingProductTest() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/api/v1/product/" + 999)
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNotFound());
    }

    @Test
    public void ProductController_DeleteProductTest() throws Exception {
        Product product = new Product();
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(100);
        Product savedProduct = productRepository.save(product);

        Long productId = savedProduct.getId();

        ResultActions resultActions = mockMvc.perform(delete("/api/v1/product/delete/" + productId)
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
    }

    @Test
    public void ProductController_DeleteNotExistingProductTest() throws Exception {
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/product/delete/" + 999)
                .contentType(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isNotFound());
    }
}

