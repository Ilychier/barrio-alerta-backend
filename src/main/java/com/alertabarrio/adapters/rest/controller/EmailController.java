package com.alertabarrio.adapters.rest.controller;

import com.alertabarrio.adapters.rest.dto.EmailRequestDTO;
import com.alertabarrio.application.command.EnviarEmailCommand;
import com.alertabarrio.domain.port.in.EnviarEmailUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    private final EnviarEmailUseCase enviarEmailUseCase;

    public EmailController(EnviarEmailUseCase enviarEmailUseCase) {
        this.enviarEmailUseCase = enviarEmailUseCase;
    }

    @PostMapping("/send-email")
    public ResponseEntity<String> sendEmail(@Valid @RequestBody EmailRequestDTO dto) {
        enviarEmailUseCase.execute(new EnviarEmailCommand(dto.toEmail(), dto.subject(), dto.body()));
        return ResponseEntity.ok("Proceso de envío completado.");
    }
}
