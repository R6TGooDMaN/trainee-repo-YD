package org.trainee.productservice.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.trainee.productservice.dto.ProductRequest;
import org.trainee.productservice.dto.ProductResponse;
import org.trainee.productservice.model.Product;
import org.trainee.productservice.service.ProductService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProductController.class)
@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;
    private Product product;
    private ProductResponse productResponse;
    private ProductRequest productRequest;

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
    public void ProductController_CreateProduct() throws Exception {
        when(productService.createProduct(any(ProductRequest.class))).thenReturn(productResponse);

        ResultActions resultActions = mockMvc.perform(post("/api/v1/product/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)));

        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", CoreMatchers.is(productResponse.getName())))
                .andExpect(jsonPath("$.description", CoreMatchers.is(productResponse.getDescription())))
                .andExpect(jsonPath("$.price", CoreMatchers.is(productResponse.getPrice())));
    }

    @Test
    public void ProductController_UpdateProduct() throws Exception {
        when(productService.updateProduct(1L,productRequest)).thenReturn(productResponse);
        ResultActions resultActions = mockMvc.perform(put("/api/v1/product/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.name", CoreMatchers.is(productResponse.getName())))
                .andExpect(jsonPath("$.description", CoreMatchers.is(productResponse.getDescription())))
                .andExpect(jsonPath("$.price", CoreMatchers.is(productResponse.getPrice())));
    }
    @Test
    public void ProductController_FindProductById() throws Exception {
        when(productService.findProduct(1L)).thenReturn(productResponse);
        ResultActions resultActions = mockMvc.perform(get("/api/v1/product/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)));
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.name", CoreMatchers.is(productResponse.getName())))
                .andExpect(jsonPath("$.description", CoreMatchers.is(productResponse.getDescription())))
                .andExpect(jsonPath("$.price", CoreMatchers.is(productResponse.getPrice())));
    }

    @Test
    public void ProductController_DeleteProduct() throws Exception {
        doNothing().when(productService).deleteProduct(1L);

        ResultActions resultActions = mockMvc.perform(delete("/api/v1/product/delete/1")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
    }

}
