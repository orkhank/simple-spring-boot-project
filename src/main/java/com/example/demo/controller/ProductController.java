package com.example.demo.controller;

import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;
import com.hazelcast.map.IMap;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final IMap<Long, Product> productCache;

    public ProductController(
            ProductService productService,
            @Qualifier("productCache") IMap<Long, Product> productCache) {
        this.productService = productService;
        this.productCache = productCache;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> fetchProduct(@PathVariable("id") Long productId) {
        // check if the product is cached
        if (productCache.containsKey(productId)) {
            return ResponseEntity.ok(productCache.get(productId));
        }

        // only access db if the product is not cached
        Optional<Product> fetched = productService.getProduct(productId);

        if (fetched.isPresent()) {
            Product product = fetched.get();
            productCache.set(productId, product);
            return ResponseEntity.ok(product);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/")
    public ResponseEntity<Product> saveProduct(@Validated @RequestBody Product product) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.saveProduct(product));
    }

    @GetMapping("/")
    public List<Product> fetchProductList() {
        return productService.fetchProductList();
    }

    @PutMapping("/{id}")
    public void updateProduct(@RequestBody Product product, @PathVariable("id") Long productId) {
        Optional<Product> updatedProduct = productService.updateProduct(product, productId);
        if (updatedProduct.isPresent() && productCache.containsKey(productId)) {
            productCache.set(productId, updatedProduct.get());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable("id") Long id) {
        productService.deleteProductById(id);
        productCache.delete(id);

        return ResponseEntity.noContent().build();
    }
}
