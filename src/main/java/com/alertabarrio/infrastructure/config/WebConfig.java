package com.alertabarrio.infrastructure.config;

import com.alertabarrio.infrastructure.security.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    public WebConfig(AuthInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns(
                        "/api/auth/login",
                        "/api/auth/register",
                        "/api/email/send-email",
                        "/api/barrios/**",
                        "/api/public/localidades/**",
                        // BC Mascotas: consultas públicas (feed, detalle, historias, catálogos)
                        "/api/mascotas/public/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/api-docs/**"
                );
    }
}
