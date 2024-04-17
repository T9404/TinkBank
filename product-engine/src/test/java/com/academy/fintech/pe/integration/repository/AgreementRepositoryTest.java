package com.academy.fintech.pe.integration.repository;

import com.academy.fintech.pe.core.service.agreement.db.agreement.Agreement;
import com.academy.fintech.pe.core.service.agreement.db.agreement.AgreementRepository;
import com.academy.fintech.pe.core.service.agreement.db.agreement.enums.AgreementStatus;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AgreementRepositoryTest {

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
    public void testSave() {
        Product product = createProduct();
        productRepository.save(product);

        Agreement agreement = createAgreement(product);
        agreementRepository.save(agreement);

        assertTrue(agreementRepository.findById(agreement.getId()).isPresent());
    }

    @Test
    public void testFindByAgreementNumber() {
        Product product = createProduct();
        productRepository.save(product);

        Agreement agreement = createAgreement(product);
        agreementRepository.save(agreement);

        assertTrue(agreementRepository.findById(agreement.getId()).isPresent());
    }

    @Test
    public void testFindByAgreementNumberNotFound() {
        assertTrue(agreementRepository.findById(UUID.randomUUID().toString()).isEmpty());
    }

    @Test
    public void testDelete() {
        Product product = createProduct();
        productRepository.save(product);

        Agreement agreement = createAgreement(product);
        agreementRepository.save(agreement);

        agreementRepository.delete(agreement);
        assertTrue(agreementRepository.findById(agreement.getId()).isEmpty());
    }

    private Agreement createAgreement(Product product) {
        Agreement agreement = new Agreement();
        agreement.setProduct(product);
        agreement.setClientId("e58ed763-928c-4155-bee9-fdbaaadc15f3");
        agreement.setInterest(new BigDecimal(8));
        agreement.setTerm(12);
        agreement.setOriginationAmount(new BigDecimal(10000));
        agreement.setPrincipalAmount(new BigDecimal(500000));
        agreement.setStatus(AgreementStatus.NEW.getStatus());
        return agreement;
    }

    private Product createProduct() {
        Product product = new Product();
        product.setCode("CL 1.0.1.1.1");
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
