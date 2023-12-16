package com.academy.fintech.origination.core.service.application.mapper;

import com.academy.fintech.application.ApplicationRequest;
import com.academy.fintech.origination.core.service.application.db.client.Users;

public final class ClientMapper {

    public static Users mapToClient(ApplicationRequest request) {
        return Users.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .salary(request.getSalary())
                .build();
    }
}
