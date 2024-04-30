package com.academy.fintech.dwh.core.application.repository;

import com.academy.fintech.dwh.core.application.repository.entity.ApplicationEntity;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository {
    void save(ApplicationEntity entity);

    Optional<ApplicationEntity> findByDataApplicationId(String id);

    List<ApplicationEntity> findAllByApplicationId(String applicationId);

    void deleteAll();
}
