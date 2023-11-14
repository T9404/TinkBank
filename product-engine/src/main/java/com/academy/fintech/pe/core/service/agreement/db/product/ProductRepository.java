package com.academy.fintech.pe.core.service.agreement.db.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    @Query("SELECT p FROM Product p WHERE p.code = ?1")
    Optional<Product> findByCode(String code);

    void deleteByCode(String code);
}
