package com.example.demo.config;

import com.example.demo.entity.Product;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public IMap<Long, Product> productCache(HazelcastInstance instance) {
        return instance.getMap("map");
    }
}
