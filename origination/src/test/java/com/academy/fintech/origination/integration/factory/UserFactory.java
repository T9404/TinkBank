package com.academy.fintech.origination.integration.factory;

import com.academy.fintech.origination.core.users.db.Users;

public final class UserFactory {

    private UserFactory() {
        throw new UnsupportedOperationException("This class should not be instantiated");
    }

    public static Users buildUser() {
        return Users.builder()
                .email("test@gmail.com")
                .firstName("test")
                .lastName("test")
                .salary(100000)
                .build();
    }
}
