package com.academy.fintech.pe.core.service.agreement;

import com.academy.fintech.pe.core.service.agreement.db.agreement.Agreement;
import com.academy.fintech.pe.core.service.agreement.db.agreement.AgreementService;
import com.academy.fintech.pe.core.service.agreement.db.product.Product;
import com.academy.fintech.pe.core.service.agreement.db.product.ProductService;
import com.academy.fintech.pe.core.service.agreement.validation.AgreementCreationValidator;
import com.example.agreement.AgreementRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.academy.fintech.pe.core.service.agreement.mapper.AgreementMapper.convertAgreementRequestToEntity;

@Service
@RequiredArgsConstructor
public class AgreementCreationService {
    private static final Logger logger = LoggerFactory.getLogger(AgreementCreationService.class);
    private final AgreementService agreementService;
    private final ProductService productService;

    public String createAgreement(AgreementRequest request) {
        Product product = productService.getProductByCode(request.getProductCode() + request.getProductVersion());

        Agreement agreement = convertAgreementRequestToEntity(request, product);
        AgreementCreationValidator.checkAgreementValidity(agreement, product);
        agreementService.saveAgreement(agreement);

        logger.info("Agreement created successfully: {}", agreement);
        return agreement.getId();
    }
}
