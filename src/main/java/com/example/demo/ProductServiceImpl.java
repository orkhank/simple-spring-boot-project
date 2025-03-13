package com.example.demo;

import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired private ProductRepository productRepository;

    @Override
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
    public Optional<Product> updateProduct(Product product, Long productId) {
        Optional<Product> foundProduct = productRepository.findById(productId);
        if (foundProduct.isEmpty()) return Optional.empty();

        Product updatedValues = foundProduct.get();
        updatedValues.setName(product.getName());
        updatedValues.setPrice(product.getPrice());

        return Optional.of(productRepository.save(updatedValues));
    }

    @Override
    public void deleteProductById(Long productId) {
        productRepository.deleteById(productId);
    }
}
