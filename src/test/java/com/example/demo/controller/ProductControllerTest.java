package com.example.demo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockitoBean private ProductService productService;

    @Test
    void testSaveProduct() throws Exception {
        Product product = new Product(1L, "Product A", 100);
        Mockito.when(productService.saveProduct(Mockito.any(Product.class))).thenReturn(product);

        mockMvc.perform(
                        post("/products/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(product)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Product A"))
                .andExpect(jsonPath("$.price").value(100.0));

        Mockito.verify(productService, Mockito.times(1)).saveProduct(Mockito.any(Product.class));
    }

    @Test
    void testFetchProductList() throws Exception {
        Product product1 = new Product(1L, "Product A", 100);
        Product product2 = new Product(2L, "Product B", 200);
        List<Product> products = Arrays.asList(product1, product2);

        Mockito.when(productService.fetchProductList()).thenReturn(products);

        mockMvc.perform(get("/products/").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Product A"))
                .andExpect(jsonPath("$[1].name").value("Product B"));

        Mockito.verify(productService, Mockito.times(1)).fetchProductList();
    }

    @Test
    void testUpdateProduct() throws Exception {
        Product product = new Product(1L, "Updated Product", 150);
        Long productId = 1L;

        mockMvc.perform(
                        put("/products/{id}", productId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(product)))
                .andExpect(status().isOk());

        Mockito.verify(productService, Mockito.times(1))
                .updateProduct(Mockito.any(Product.class), Mockito.eq(productId));
    }

    @Test
    void testDeleteProductById() throws Exception {
        Long productId = 1L;

        mockMvc.perform(delete("/products/{id}", productId)).andExpect(status().isNoContent());

        Mockito.verify(productService, Mockito.times(1)).deleteProductById(productId);
    }
}
