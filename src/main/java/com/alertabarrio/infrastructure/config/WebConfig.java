package com.alertabarrio.infrastructure.config;

import com.alertabarrio.infrastructure.security.AuthInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.time.Duration;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;
    private final String uploadsDir;

    public WebConfig(AuthInterceptor authInterceptor,
                     @Value("${app.uploads.dir:uploads}") String uploadsDir) {
        this.authInterceptor = authInterceptor;
        this.uploadsDir = uploadsDir;
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

    /**
     * Sirve las fotos en dev (el backend persiste en disco local).
     * En producción Nginx las sirve directamente (location /uploads/)
     * y esta ruta nunca se alcanza — no hay conflicto.
     * Cache-Control 30 días: las fotos son inmutables (UUID) → el navegador
     * no re-descarga al remontar celdas del feed (evita el micro lag del scroll).
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String ubicacion = Path.of(uploadsDir).toAbsolutePath().toUri().toString();
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(ubicacion)
                .setCacheControl(CacheControl.maxAge(Duration.ofDays(30)).cachePublic());
    }
}
