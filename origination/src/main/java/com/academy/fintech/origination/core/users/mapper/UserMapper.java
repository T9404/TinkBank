package com.academy.fintech.origination.core.users.mapper;

import com.academy.fintech.application.ApplicationRequest;
import com.academy.fintech.origination.core.users.db.Users;

public final class UserMapper {

    public static Users mapToUser(ApplicationRequest request) {
        return Users.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .salary(request.getSalary())
                .build();
    }
}
