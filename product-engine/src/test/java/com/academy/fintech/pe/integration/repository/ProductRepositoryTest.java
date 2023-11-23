package com.academy.fintech.pe.integration.repository;

import com.academy.fintech.pe.core.service.agreement.db.product.Product;
import com.academy.fintech.pe.core.service.agreement.db.product.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class  ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @BeforeEach
    void setup(){
        productRepository.deleteByCode("CL 1.1.10");
    }

    @Test
    public void testSave() {
        Product product = createProduct();
        productRepository.save(product);
        assertTrue(productRepository.findByCode("CL 1.1.10").isPresent());
    }

    @Test
    public void testFindById() {
        Product product = createProduct();
        productRepository.save(product);
        Optional<Product> productOptional = productRepository.findByCode(product.getCode());
        assertTrue(productOptional.isPresent());
    }

    @Test
    public void testFindByIdNotFound() {
        Optional<Product> productOptional = productRepository.findByCode("CL 2.0");
        assertTrue(productOptional.isEmpty());
    }

    @Test
    public void testDelete() {
        Product product = createProduct();
        productRepository.save(product);
        productRepository.deleteByCode(product.getCode());
        assertTrue(productRepository.findByCode(product.getCode()).isEmpty());
    }

    private Product createProduct() {
        Product product = new Product();
        product.setCode("CL 1.1.10");
        product.setMinTerm(12);
        product.setMaxTerm(60);
        product.setMinPrincipalAmount(new BigDecimal(10000));
        product.setMaxPrincipalAmount(new BigDecimal(500000));
        product.setMinInterest(new BigDecimal(8));
        product.setMaxInterest(new BigDecimal(12));
        product.setMinOriginationAmount(new BigDecimal(10000));
        product.setMaxOriginationAmount(new BigDecimal(500000));
        return product;
    }
}
