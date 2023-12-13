package com.academy.fintech.pe.core.service.agreement.db.product;

import com.academy.fintech.pe.core.service.agreement.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Product getProductByCode(String productCode) {
        return productRepository.findByCode(productCode).orElseThrow(ProductNotFoundException::new);
    }
}
