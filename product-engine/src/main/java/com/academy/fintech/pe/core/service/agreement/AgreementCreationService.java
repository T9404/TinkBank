package com.academy.fintech.pe.core.service.agreement;

import com.academy.fintech.pe.core.service.agreement.db.agreement.Agreement;
import com.academy.fintech.pe.core.service.agreement.db.agreement.AgreementService;
import com.academy.fintech.pe.core.service.agreement.db.product.Product;
import com.academy.fintech.pe.core.service.agreement.db.product.ProductService;
import com.academy.fintech.pe.core.service.agreement.validation.AgreementCreationValidator;
import com.example.agreement.AgreementRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.academy.fintech.pe.core.service.agreement.util.AgreementMapper.convertAgreementRequestToEntity;

@Service
public class AgreementCreationService {
    private static final Logger logger = LoggerFactory.getLogger(AgreementCreationService.class);
    private AgreementService agreementService;
    private ProductService productService;

    @Autowired
    public void setAgreementService(AgreementService agreementService) {
        this.agreementService = agreementService;
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    public String createAgreement(AgreementRequest request) {
        Agreement agreement = convertAgreementRequestToEntity(request);

        Product product = productService.getProductByCode(agreement.getProductCode());
        AgreementCreationValidator.checkAgreementValidity(agreement, product);
        agreementService.saveAgreement(agreement);

        logger.info("Agreement created successfully: {}", agreement);
        return agreement.getId().toString();
    }
}
