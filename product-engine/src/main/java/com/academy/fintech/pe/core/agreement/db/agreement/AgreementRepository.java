package com.academy.fintech.pe.core.agreement.db.agreement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AgreementRepository extends JpaRepository<Agreement, String> {
    List<Agreement> findAllByClientId(String clientId);
    Optional<Agreement> findById(String agreementId);
}
