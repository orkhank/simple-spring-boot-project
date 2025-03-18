package com.example.demo.config;

import com.example.demo.CachedProductService;
import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public IMap<Long, Product> productCache(HazelcastInstance instance) {
        return instance.getMap("map");
    }

    @Bean
    public ProductService cachedProductService(
            ProductService productService,
            @Qualifier("productCache") IMap<Long, Product> productCache) {
        return new CachedProductService(productService, productCache);
    }
}
