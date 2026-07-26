package com.alertabarrio.infrastructure.email;

import com.alertabarrio.domain.exception.EmailNoEnviadoException;
import com.alertabarrio.domain.model.valueobject.Email;
import com.alertabarrio.domain.port.out.EmailPort;
import com.alertabarrio.infrastructure.email.dto.BrevoPayloadDTO;
import com.alertabarrio.infrastructure.email.dto.BrevoSenderDTO;
import com.alertabarrio.infrastructure.email.dto.BrevoToDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class BrevoEmailAdapter implements EmailPort {

    private static final String BREVO_URL = "https://api.brevo.com/v3/smtp/email";

    private final RestTemplate restTemplate;
    private final BrevoProperties properties;

    public BrevoEmailAdapter(RestTemplate restTemplate, BrevoProperties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    @Override
    public void enviar(Email destinatario, String asunto, String cuerpoHtml) {
        BrevoSenderDTO sender = new BrevoSenderDTO(properties.getSender().getName(), properties.getSender().getEmail());
        BrevoToDTO recipient = new BrevoToDTO(destinatario.value());
        BrevoPayloadDTO payload = new BrevoPayloadDTO(sender, List.of(recipient), asunto, cuerpoHtml);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", properties.getApiKey());

        HttpEntity<BrevoPayloadDTO> httpEntity = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(BREVO_URL, httpEntity, String.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new EmailNoEnviadoException("Brevo responded with " + response.getStatusCode(), null);
            }
        } catch (EmailNoEnviadoException e) {
            throw e;
        } catch (Exception e) {
            throw new EmailNoEnviadoException("No se pudo enviar el correo: " + e.getMessage(), e);
        }
    }
}
