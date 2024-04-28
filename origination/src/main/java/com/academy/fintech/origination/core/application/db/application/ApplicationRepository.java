package com.academy.fintech.origination.core.application.db.application;

import com.academy.fintech.origination.core.users.db.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, String> {
    boolean existsByUsersAndStatusAndRequestedDisbursementAmount(Users user, String status, int requestedDisbursementAmount);
    Optional<Application> findByUsersAndStatusAndRequestedDisbursementAmount(Users user, String status, int requestedDisbursementAmount);

    @Query("SELECT a FROM Application a JOIN FETCH a.users WHERE a.status = ?1")
    Iterable<Application> findAllByStatus(String status);
}
