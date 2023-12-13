package com.academy.fintech.origination.core.service.application.db.client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Users, String> {
    Optional<Users> findByEmail(String email);
}
