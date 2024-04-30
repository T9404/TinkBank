package com.academy.fintech.pe.unit.service;

import com.academy.fintech.pe.core.agreement.db.agreement.Agreement;
import com.academy.fintech.pe.core.agreement.db.product.Product;
import com.academy.fintech.pe.core.agreement.exception.AgreementValidationException;
import com.academy.fintech.pe.core.agreement.validation.AgreementCreationValidator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AgreementCreationValidatorTest {

    @Test
    public void testValidateAgreement() {
        Product product = getProduct();

        Agreement agreement = new Agreement();
        agreement.setProduct(product);
        agreement.setClientId(String.valueOf(UUID.randomUUID()));
        agreement.setInterest(new BigDecimal("8"));
        agreement.setTerm(12);
        agreement.setPrincipalAmount(new BigDecimal("500000"));
        agreement.setOriginationAmount(new BigDecimal("10000"));
        agreement.setStatus("ACTIVE");

        assertDoesNotThrow(() -> AgreementCreationValidator.checkAgreementValidity(agreement, product));
    }

    @Test
    public void testInvalidMinTerm() {
        Product product = getProduct();

        Agreement agreement = new Agreement();
        agreement.setProduct(product);
        agreement.setClientId(String.valueOf(UUID.randomUUID()));
        agreement.setInterest(new BigDecimal("8"));
        agreement.setTerm(11);
        agreement.setPrincipalAmount(new BigDecimal("500000"));
        agreement.setOriginationAmount(new BigDecimal("10000"));
        agreement.setStatus("ACTIVE");

        assertThrows(AgreementValidationException.class, () -> AgreementCreationValidator.checkAgreementValidity(agreement, product));
    }

    @Test
    public void testInvalidMaxTerm() {
        Product product = getProduct();

        Agreement agreement = new Agreement();
        agreement.setProduct(product);
        agreement.setClientId(String.valueOf(UUID.randomUUID()));
        agreement.setInterest(new BigDecimal("8"));
        agreement.setTerm(25);
        agreement.setPrincipalAmount(new BigDecimal("500000"));
        agreement.setOriginationAmount(new BigDecimal("10000"));
        agreement.setStatus("ACTIVE");

        assertThrows(AgreementValidationException.class, () -> AgreementCreationValidator.checkAgreementValidity(agreement, product));
    }

    @Test
    public void testInvalidMinPrincipalAmount() {
        Product product = getProduct();

        Agreement agreement = new Agreement();
        agreement.setProduct(product);
        agreement.setClientId(String.valueOf(UUID.randomUUID()));
        agreement.setInterest(new BigDecimal("8"));
        agreement.setTerm(12);
        agreement.setPrincipalAmount(new BigDecimal("499999"));
        agreement.setOriginationAmount(new BigDecimal("10000"));
        agreement.setStatus("ACTIVE");

        assertThrows(AgreementValidationException.class, () -> AgreementCreationValidator.checkAgreementValidity(agreement, product));
    }

    @Test
    public void testInvalidMaxPrincipalAmount() {
        Product product = getProduct();

        Agreement agreement = new Agreement();
        agreement.setProduct(product);
        agreement.setClientId(String.valueOf(UUID.randomUUID()));
        agreement.setInterest(new BigDecimal("8"));
        agreement.setTerm(12);
        agreement.setPrincipalAmount(new BigDecimal("1000001"));
        agreement.setOriginationAmount(new BigDecimal("10000"));
        agreement.setStatus("ACTIVE");

        assertThrows(AgreementValidationException.class, () -> AgreementCreationValidator.checkAgreementValidity(agreement, product));
    }

    @Test
    public void testInvalidMinInterest() {
        Product product = getProduct();

        Agreement agreement = new Agreement();
        agreement.setProduct(product);
        agreement.setClientId(String.valueOf(UUID.randomUUID()));
        agreement.setInterest(new BigDecimal("7.99"));
        agreement.setTerm(12);
        agreement.setPrincipalAmount(new BigDecimal("500000"));
        agreement.setOriginationAmount(new BigDecimal("10000"));
        agreement.setStatus("ACTIVE");

        assertThrows(AgreementValidationException.class, () -> AgreementCreationValidator.checkAgreementValidity(agreement, product));
    }

    @Test
    public void testInvalidMaxInterest() {
        Product product = getProduct();

        Agreement agreement = new Agreement();
        agreement.setProduct(product);
        agreement.setClientId(String.valueOf(UUID.randomUUID()));
        agreement.setInterest(new BigDecimal("12.01"));
        agreement.setTerm(12);
        agreement.setPrincipalAmount(new BigDecimal("500000"));
        agreement.setOriginationAmount(new BigDecimal("10000"));
        agreement.setStatus("ACTIVE");

        assertThrows(AgreementValidationException.class, () -> AgreementCreationValidator.checkAgreementValidity(agreement, product));
    }

    @Test
    public void testInvalidMinOriginationAmount() {
        Product product = getProduct();

        Agreement agreement = new Agreement();
        agreement.setProduct(product);
        agreement.setClientId(String.valueOf(UUID.randomUUID()));
        agreement.setInterest(new BigDecimal("8"));
        agreement.setTerm(12);
        agreement.setPrincipalAmount(new BigDecimal("500000"));
        agreement.setOriginationAmount(new BigDecimal("9999"));
        agreement.setStatus("ACTIVE");

        assertThrows(AgreementValidationException.class, () -> AgreementCreationValidator.checkAgreementValidity(agreement, product));
    }

    @Test
    public void testInvalidMaxOriginationAmount() {
        Product product = getProduct();

        Agreement agreement = new Agreement();
        agreement.setProduct(product);
        agreement.setClientId(String.valueOf(UUID.randomUUID()));
        agreement.setInterest(new BigDecimal("8"));
        agreement.setTerm(12);
        agreement.setPrincipalAmount(new BigDecimal("500000"));
        agreement.setOriginationAmount(new BigDecimal("50001"));
        agreement.setStatus("ACTIVE");

        assertThrows(AgreementValidationException.class, () -> AgreementCreationValidator.checkAgreementValidity(agreement, product));
    }

    private Product getProduct() {
        Product product = new Product();
        product.setCode("CL 1.0");
        product.setMinTerm(12);
        product.setMaxTerm(24);
        product.setMinPrincipalAmount(new BigDecimal("500000"));
        product.setMaxPrincipalAmount(new BigDecimal("1000000"));
        product.setMinInterest(new BigDecimal("8"));
        product.setMaxInterest(new BigDecimal("12"));
        product.setMinOriginationAmount(new BigDecimal("10000"));
        product.setMaxOriginationAmount(new BigDecimal("50000"));
        return product;
    }
}
