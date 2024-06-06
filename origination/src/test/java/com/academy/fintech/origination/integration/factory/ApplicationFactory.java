package com.academy.fintech.origination.integration.factory;

import com.academy.fintech.origination.core.application.db.application.Application;
import com.academy.fintech.origination.core.application.db.application.enums.ApplicationStatus;
import com.academy.fintech.origination.core.users.db.Users;

public final class ApplicationFactory {

    private ApplicationFactory() {
        throw new UnsupportedOperationException("This class should not be instantiated");
    }

    public static Application buildApplication(Users savedUser) {
        return Application.builder()
                .users(savedUser)
                .status(ApplicationStatus.ACTIVE.getStatus())
                .requestedDisbursementAmount(100000)
                .build();
    }
}
