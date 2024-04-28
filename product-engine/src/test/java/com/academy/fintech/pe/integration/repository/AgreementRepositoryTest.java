package com.academy.fintech.pe.integration.repository;

import com.academy.fintech.pe.core.agreement.db.agreement.Agreement;
import com.academy.fintech.pe.core.agreement.db.agreement.AgreementRepository;
import com.academy.fintech.pe.core.agreement.db.product.Product;
import com.academy.fintech.pe.core.agreement.db.product.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static com.academy.fintech.pe.integration.factory.AgreementFactory.createAgreement;
import static com.academy.fintech.pe.integration.factory.ProductFactory.createProduct;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AgreementRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    AgreementRepository agreementRepository;

    @BeforeEach
    void setup() {
        productRepository.deleteAll();
        agreementRepository.deleteAll();
    }

    @Test
    void testSave() {
        Product product = createProduct();
        productRepository.save(product);

        Agreement agreement = createAgreement(product);
        agreementRepository.save(agreement);

        assertTrue(agreementRepository.findById(agreement.getId()).isPresent());
    }

    @Test
    void testFindByAgreementNumber() {
        Product product = createProduct();
        productRepository.save(product);

        Agreement agreement = createAgreement(product);
        agreementRepository.save(agreement);

        assertTrue(agreementRepository.findById(agreement.getId()).isPresent());
    }

    @Test
    void testFindByAgreementNumberNotFound() {
        assertTrue(agreementRepository.findById(UUID.randomUUID().toString()).isEmpty());
    }

    @Test
    void testDelete() {
        Product product = createProduct();
        productRepository.save(product);

        Agreement agreement = createAgreement(product);
        agreementRepository.save(agreement);

        agreementRepository.delete(agreement);
        assertTrue(agreementRepository.findById(agreement.getId()).isEmpty());
    }
}
