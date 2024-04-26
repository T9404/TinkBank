package com.academy.fintech.dwh.core.application.data.repository;

import com.academy.fintech.dwh.core.application.data.repository.entity.ApplicationDataEntity;

import java.util.Optional;

public interface ApplicationDataRepository {
    void save(ApplicationDataEntity entity);
    Optional<ApplicationDataEntity> findById(String id);
    void deleteAll();
}
