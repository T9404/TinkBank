package com.academy.fintech.dwh.core.agreement.repository;

import com.academy.fintech.dwh.core.agreement.repository.entity.AgreementEntity;

import java.util.List;
import java.util.Optional;

public interface AgreementRepository {
    void save(AgreementEntity agreement);

    Optional<AgreementEntity> findByDataAgreementId(String dataAgreementId);

    List<AgreementEntity> findAllByAgreementNumber(String agreementNumber);

    void deleteAll();
}
