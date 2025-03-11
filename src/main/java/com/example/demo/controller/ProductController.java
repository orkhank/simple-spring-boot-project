package com.example.demo.controller;

import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/")
    public ResponseEntity<Product> saveProduct(@Validated @RequestBody Product product) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.saveProduct(product));
    }

    @GetMapping("/")
    public List<Product> fetchDepartmentList() {
        return productService.fetchProductList();
    }

    @PutMapping("/{id}")
    public void updateProduct(@RequestBody Product product, @PathVariable("id") Long productId) {
        productService.updateProduct(product, productId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable("id") Long id) {
        productService.deleteProductById(id);

        return ResponseEntity.noContent().build();
    }
}
