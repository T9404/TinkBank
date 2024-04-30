package com.academy.fintech.pe.integration.repository;

import com.academy.fintech.pe.core.agreement.db.product.Product;
import com.academy.fintech.pe.core.agreement.db.product.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static com.academy.fintech.pe.integration.factory.ProductFactory.createProduct;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class  ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @BeforeEach
    void setup(){
        productRepository.deleteAll();
    }

    @Test
    void testSave() {
        Product product = createProduct();
        productRepository.save(product);
        assertTrue(productRepository.findByCode(product.getCode()).isPresent());
    }

    @Test
    void testFindById() {
        Product product = createProduct();
        productRepository.save(product);
        Optional<Product> productOptional = productRepository.findByCode(product.getCode());
        assertTrue(productOptional.isPresent());
    }

    @Test
    void testFindByIdNotFound() {
        Optional<Product> productOptional = productRepository.findByCode("CL 2.0");
        assertTrue(productOptional.isEmpty());
    }

    @Test
    void testDelete() {
        Product product = createProduct();
        productRepository.save(product);
        productRepository.deleteByCode(product.getCode());
        assertTrue(productRepository.findByCode(product.getCode()).isEmpty());
    }

}
