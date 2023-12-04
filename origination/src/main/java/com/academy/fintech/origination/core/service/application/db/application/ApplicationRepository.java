package com.academy.fintech.origination.core.service.application.db.application;

import com.academy.fintech.origination.core.service.application.db.client.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, String> {
    boolean existsByClientAndStatusAndRequestedDisbursementAmount(Client client, String status, int requestedDisbursementAmount);
    Optional<Application> findByClientAndStatusAndRequestedDisbursementAmount(Client client, String status,
                                                                              int requestedDisbursementAmount);
    @Query("SELECT a FROM Application a JOIN FETCH a.client WHERE a.status = ?1")
    Iterable<Application> findAllByStatus(String status);
}
