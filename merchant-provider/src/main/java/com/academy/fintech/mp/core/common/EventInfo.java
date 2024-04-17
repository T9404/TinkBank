package com.academy.fintech.mp.core.common;

import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;

public interface EventInfo {
    HttpStatus getStatus();

    Level getLevel();
}

