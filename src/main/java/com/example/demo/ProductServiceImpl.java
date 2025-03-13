package com.example.demo;

import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    private static final String PRODUCT_CACHE = "PRODUCT_CACHE";
    @Autowired private ProductRepository productRepository;

    @Override
    @Cacheable(value = PRODUCT_CACHE, key = "#productId")
    public Optional<Product> getProduct(Long productId) {
        return productRepository.findById(productId);
    }

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> fetchProductList() {
        return (List<Product>) productRepository.findAll();
    }

    @Transactional
    @Override
    @CachePut(value = PRODUCT_CACHE, key = "#productId")
    public Optional<Product> updateProduct(Product product, Long productId) {
        Optional<Product> foundProduct = productRepository.findById(productId);
        if (foundProduct.isEmpty()) return Optional.empty();

        Product updatedValues = foundProduct.get();
        updatedValues.setName(product.getName());
        updatedValues.setPrice(product.getPrice());

        return Optional.of(productRepository.save(updatedValues));
    }

    @Override
    @CacheEvict(value = PRODUCT_CACHE, key = "#productId")
    public void deleteProductById(Long productId) {
        productRepository.deleteById(productId);
    }
}
