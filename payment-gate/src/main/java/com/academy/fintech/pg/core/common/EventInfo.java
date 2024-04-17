package com.academy.fintech.pg.core.common;

import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;

public interface EventInfo {
    HttpStatus getStatus();
    Level getLevel();
}
