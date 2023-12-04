package com.academy.fintech.origination.core.service.application.mapper;

import com.academy.fintech.application.ApplicationRequest;
import com.academy.fintech.origination.core.service.application.db.client.Client;

public final class ClientMapper {

    public static Client mapToClient(ApplicationRequest request) {
        return Client.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .salary(request.getSalary())
                .build();
    }
}
