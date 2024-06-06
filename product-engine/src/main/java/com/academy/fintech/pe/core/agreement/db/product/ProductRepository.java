package com.academy.fintech.pe.core.agreement.db.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    Optional<Product> findByCode(String code);
    void deleteByCode(String code);
}
