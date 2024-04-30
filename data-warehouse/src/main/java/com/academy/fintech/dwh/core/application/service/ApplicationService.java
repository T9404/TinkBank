package com.academy.fintech.dwh.core.application.service;

import com.academy.fintech.dwh.core.application.repository.ApplicationRepository;
import com.academy.fintech.dwh.core.application.repository.entity.ApplicationEntity;
import com.academy.fintech.dwh.public_interface.application.ApplicationExportDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.academy.fintech.dwh.core.converter.LocalDateConverter.convertToLocalDate;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;

    public void save(ApplicationExportDto dto) {
        var existingEntity = applicationRepository.findByDataApplicationId(dto.dataApplicationId());

        if (existingEntity.isPresent()) {
            return;
        }

        var entity = ApplicationEntity.builder()
                .dataApplicationId(dto.dataApplicationId())
                .applicationId(dto.applicationId())
                .status(dto.status())
                .businessDate(convertToLocalDate(dto.createdAt()))
                .build();

        applicationRepository.save(entity);
    }
}
