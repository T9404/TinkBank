package com.academy.fintech.origination.core.service.application.db.application;

import com.academy.fintech.origination.core.service.application.db.client.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, String> {
    boolean existsByUsersAndStatusAndRequestedDisbursementAmount(Users users, String status, int requestedDisbursementAmount);
    Optional<Application> findByUsersAndStatusAndRequestedDisbursementAmount(Users users, String status, int requestedDisbursementAmount);
}
