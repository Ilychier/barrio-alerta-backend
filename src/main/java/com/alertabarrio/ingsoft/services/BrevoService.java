package com.alertabarrio.ingsoft.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alertabarrio.ingsoft.models.dtos.BrevoPayloadDTO;
import com.alertabarrio.ingsoft.models.dtos.BrevoSenderDTO;
import com.alertabarrio.ingsoft.models.dtos.BrevoToDTO;
import com.alertabarrio.ingsoft.models.dtos.EmailRequestDTO;


@Service
public class BrevoService {

// Inyectamos los valores que guardamos en el application.properties
    @Value("${brevo.api.key}")
    private String apiKey;

    @Value("${brevo.sender.email}")
    private String senderEmail;

    @Value("${brevo.sender.name}")
    private String senderName;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String BREVO_URL = "https://api.brevo.com/v3/smtp/email";

    public void sendEmail(EmailRequestDTO request) {
        // 1. Construimos el remitente y destinatario usando nuestros moldes
        BrevoSenderDTO sender = new BrevoSenderDTO(senderName, senderEmail);
        BrevoToDTO recipient = new BrevoToDTO(request.toEmail());
        
        // 2. Armamos el cuerpo final que Brevo entiende
        BrevoPayloadDTO payload = new BrevoPayloadDTO(
            sender,
            List.of(recipient),
            request.subject(),
            "" + request.body() + ""
        );

        // 3. Configuramos las cabeceras (Headers) de la petición HTTP. 
        // Aquí es donde le pasamos la API Key a Brevo para que sepa quiénes somos.
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", apiKey);

        // 4. Empaquetamos todo en una entidad de petición
        HttpEntity httpEntity = new HttpEntity<>(payload, headers);

        try {
            // 5. Hacemos el disparo (POST) hacia la API de Brevo
            ResponseEntity response = restTemplate.postForEntity(BREVO_URL, httpEntity, String.class);
            
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("¡Correo enviado con éxito a Brevo!");
            }
        } catch (Exception e) {
            System.err.println("Error al conectar con Brevo: " + e.getMessage());
            throw new RuntimeException("No se pudo enviar el correo");
        }
    }

}
