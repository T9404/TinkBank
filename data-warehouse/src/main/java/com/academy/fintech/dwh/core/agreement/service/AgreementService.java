package com.academy.fintech.dwh.core.agreement.service;

import com.academy.fintech.dwh.core.agreement.repository.AgreementRepository;
import com.academy.fintech.dwh.core.agreement.repository.entity.AgreementEntity;
import com.academy.fintech.dwh.public_interface.agreement.AgreementExportDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static com.academy.fintech.dwh.core.converter.LocalDateConverter.convertToLocalDate;

@Service
@AllArgsConstructor
public class AgreementService {
    private AgreementRepository agreementRepository;

    public void save(AgreementExportDto dto) {
        var existingEntity = agreementRepository.findByDataAgreementId(dto.dataAgreementId());

        if (existingEntity.isPresent()) {
            return;
        }

        var entity = AgreementEntity.builder()
                .dataAgreementId(dto.dataAgreementId())
                .agreementNumber(dto.agreementNumber())
                .amount(dto.amount())
                .businessDate(convertToLocalDate(dto.businessDate()))
                .status(dto.status())
                .build();

        agreementRepository.save(entity);
    }

}
