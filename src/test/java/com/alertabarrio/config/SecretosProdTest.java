package com.alertabarrio.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import org.junit.jupiter.api.Test;

/**
 * Candado contra regresiones: los secretos no viajan en el JAR.
 * En prod las claves se exigen desde el entorno, sin valor por defecto.
 */
class SecretosProdTest {

    private Properties cargar(String recurso) throws Exception {
        Properties p = new Properties();
        try (InputStream in = getClass().getResourceAsStream(recurso)) {
            if (in == null) {
                throw new IllegalStateException("No esta en el classpath: " + recurso);
            }
            try (Reader r = new InputStreamReader(in, StandardCharsets.UTF_8)) {
                p.load(r);
            }
        }
        return p;
    }

    @Test
    void prodExigeLasClavesDelEntorno() throws Exception {
        Properties prod = cargar("/application-prod.properties");

        assertEquals("${BREVO_API_KEY}", prod.getProperty("brevo.api-key"));
        assertEquals("${BREVO_SENDER_EMAIL}", prod.getProperty("brevo.sender.email"));
        assertEquals("${BREVO_SENDER_NAME}", prod.getProperty("brevo.sender.name"));
        assertEquals("${JWT_SECRET}", prod.getProperty("jwt.secret"));
    }

    @Test
    void laBaseCompartidaNoLlevaSecretos() throws Exception {
        Properties base = cargar("/application.properties");

        assertEquals("${BREVO_API_KEY:}", base.getProperty("brevo.api-key"));
        assertEquals("${BREVO_SENDER_EMAIL:}", base.getProperty("brevo.sender.email"));
    }
}
