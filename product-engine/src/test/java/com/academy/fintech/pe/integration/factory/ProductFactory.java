package com.academy.fintech.pe.integration.factory;

import com.academy.fintech.pe.core.agreement.db.product.Product;

import java.math.BigDecimal;

public final class ProductFactory {

    private ProductFactory() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

    public static Product createProduct() {
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
