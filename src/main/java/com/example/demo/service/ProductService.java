package com.example.demo.service;

import com.example.demo.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    Optional<Product> getProduct(Long productId);

    Product saveProduct(Product product);

    List<Product> fetchProductList();

    Optional<Product> updateProduct(Product product, Long productId);

    void deleteProductById(Long productId);
}
