package com.academy.fintech.dwh.core.application.data.service;

import com.academy.fintech.dwh.core.application.data.repository.ApplicationDataRepository;
import com.academy.fintech.dwh.core.application.data.repository.entity.ApplicationDataEntity;
import com.academy.fintech.dwh.public_interface.application.ApplicationDataDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationDataService {
    private final ApplicationDataRepository applicationDataRepository;

    public void save(ApplicationDataDto dto) {
        var entity = ApplicationDataEntity.builder()
                .id(dto.id())
                .content(dto.content())
                .businessDate(dto.businessDate())
                .build();

        var existingEntity = applicationDataRepository.findById(entity.id());

        if (existingEntity.isPresent()) {
            return;
        }

        applicationDataRepository.save(entity);
    }
}
