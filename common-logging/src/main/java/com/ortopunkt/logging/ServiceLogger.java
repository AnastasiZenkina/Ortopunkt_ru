package com.ortopunkt.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceLogger {

    private final Logger log;
    private final String serviceName;

    public ServiceLogger(Class<?> clazz, String serviceName) {
        this.log = LoggerFactory.getLogger(clazz);
        this.serviceName = serviceName;
    }

    public void info(String message) {
        log.info("[{}] {}", serviceName, LoggingUtils.info(message));
    }

    public void warn(String message) {
        log.warn("[{}] {}", serviceName, LoggingUtils.warn(message));
    }

    public void error(String message) {
        log.error("[{}] {}", serviceName, LoggingUtils.error(message));
    }
}
