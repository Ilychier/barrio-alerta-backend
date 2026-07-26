package com.alertabarrio.infrastructure.config;

import org.springframework.context.annotation.Configuration;

/**
 * Wires hexagonal adapters and use cases.
 * Most beans are auto-discovered via @UseCase, @Repository, and @Mapper annotations.
 * This config is reserved for beans that need explicit wiring
 * (e.g., RestTemplate, Clock, third-party clients).
 */
@Configuration
public class BeanConfig {

    // Future: declare beans for EmailPort, TokenServicePort, PasswordEncoderPort, Clock, etc.
}
