package com.example.demo;

import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;
import com.hazelcast.map.IMap;

import java.util.List;
import java.util.Optional;

public class CachedProductService implements ProductService {
    private final IMap<Long, Product> productCache;
    private final ProductService wrappedService;

    public CachedProductService(ProductService productService, IMap<Long, Product> productCache) {
        this.wrappedService = productService;
        this.productCache = productCache;
    }

    @Override
    public Optional<Product> getProduct(Long productId) {
        // check if the product is cached
        if (productCache.containsKey(productId)) {
            return Optional.of(productCache.get(productId));
        }

        // only access db if the product is not cached
        Optional<Product> fetched = wrappedService.getProduct(productId);
        if (fetched.isPresent()) {
            Product product = fetched.get();
            productCache.set(productId, product);
            return Optional.of(product);
        }
        return Optional.empty();
    }

    @Override
    public Product saveProduct(Product product) {
        // no caching
        return wrappedService.saveProduct(product);
    }

    @Override
    public List<Product> fetchProductList() {
        // no caching
        return wrappedService.fetchProductList();
    }

    @Override
    public Optional<Product> updateProduct(Product product, Long productId) {
        Optional<Product> updatedProduct = wrappedService.updateProduct(product, productId);
        if (updatedProduct.isPresent() && productCache.containsKey(productId)) {
            // update cache to prevent stale data
            productCache.set(productId, updatedProduct.get());
        }
        return updatedProduct;
    }

    @Override
    public void deleteProductById(Long productId) {
        wrappedService.deleteProductById(productId);
        productCache.delete(productId); // delete from cache as well
    }
}
