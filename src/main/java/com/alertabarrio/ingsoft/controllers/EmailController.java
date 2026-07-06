package com.alertabarrio.ingsoft.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alertabarrio.ingsoft.models.dtos.EmailRequestDTO;
import com.alertabarrio.ingsoft.services.BrevoService;

@RestController
@RequestMapping("/api/email")
@CrossOrigin(origins = "*") 
public class EmailController {

private final BrevoService brevoService;

    // Inyección de dependencias por constructor
    public EmailController(BrevoService brevoService) {
        this.brevoService = brevoService;
    }

    @PostMapping("/send-email")
    public ResponseEntity sendEmailFromMobile(@RequestBody EmailRequestDTO request) {
        // Validaciones básicas por si falta algún dato
        if (request.toEmail() == null || request.toEmail().isBlank()) {
            return ResponseEntity.badRequest().body("El correo de destino es obligatorio");
        }

        // Delegamos el trabajo pesado al servicio
        brevoService.sendEmail(request);

        // Respondemos al celular que todo salió perfecto
        return ResponseEntity.ok("Proceso de envío completado.");
    }

}
