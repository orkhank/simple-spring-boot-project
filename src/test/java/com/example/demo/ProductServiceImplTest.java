package com.example.demo;

import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    @Test
    void testSaveProduct() {
        Product product =
                Product.builder().name("Test Product").price(100).build();

        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenReturn(product);

        Product savedProduct = productService.saveProduct(product);

        assertNotNull(savedProduct);
        assertEquals("Test Product", savedProduct.getName());
        assertEquals(100.0, savedProduct.getPrice());
        Mockito.verify(productRepository, Mockito.times(1)).save(product);
    }

    @Test
    void testFetchProductList() {
        Product product1 =
                Product.builder().name("Product 1").price(50).build();
        Product product2 =
                Product.builder().name("Product 2").price(150).build();

        Mockito.when(productRepository.findAll()).thenReturn(Arrays.asList(product1,
                product2));

        List<Product> productList = productService.fetchProductList();

        assertNotNull(productList);
        assertEquals(2, productList.size());
        assertEquals("Product 1", productList.get(0).getName());
        assertEquals("Product 2", productList.get(1).getName());
        Mockito.verify(productRepository, Mockito.times(1)).findAll();
    }

    @Test
    void testUpdateProduct() {
        Product existingProduct =
                Product.builder().name("Old Product").price(50).build();
        Product updatedProduct =
                Product.builder().name("Updated Product").price(75).build();

        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenReturn(existingProduct);

        productService.updateProduct(updatedProduct, 1L);

        assertEquals("Updated Product", existingProduct.getName());
        assertEquals(75.0, existingProduct.getPrice());
        Mockito.verify(productRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(productRepository, Mockito.times(1)).save(existingProduct);
    }

    @Test
    void testDeleteProductById() {
        Long productId = 1L;

        Mockito.doNothing().when(productRepository).deleteById(productId);

        productService.deleteProductById(productId);

        Mockito.verify(productRepository, Mockito.times(1)).deleteById(productId);
    }
}
