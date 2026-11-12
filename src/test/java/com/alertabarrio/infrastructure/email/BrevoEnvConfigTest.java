package com.alertabarrio.infrastructure.email;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * La API key y el remitente de Brevo se leen del entorno, nunca del repo.
 */
@SpringBootTest(properties = {
        "BREVO_API_KEY=clave-de-prueba",
        "BREVO_SENDER_EMAIL=no-reply@example.com",
        "BREVO_SENDER_NAME=Pruebas"
})
@ActiveProfiles("test")
class BrevoEnvConfigTest {

    @Autowired
    private BrevoProperties properties;

    @Test
    void tomaLaConfiguracionDeBrevoDelEntorno() {
        assertEquals("clave-de-prueba", properties.getApiKey());
        assertEquals("no-reply@example.com", properties.getSender().getEmail());
        assertEquals("Pruebas", properties.getSender().getName());
    }
}
