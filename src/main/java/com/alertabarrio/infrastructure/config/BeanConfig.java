package com.alertabarrio.infrastructure.config;

import com.alertabarrio.infrastructure.email.BrevoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Clock;

/**
 * Wires hexagonal adapters and use cases.
 * Most beans are auto-discovered via @UseCase, @Repository, @Component, and @Mapper annotations.
 * This config is reserved for beans that need explicit wiring
 * (e.g., RestTemplate, Clock, third-party clients).
 */
@Configuration
@EnableConfigurationProperties(BrevoProperties.class)
public class BeanConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}
