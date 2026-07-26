package com.alertabarrio.application.usecase;

import com.alertabarrio.application.command.EnviarEmailCommand;
import com.alertabarrio.domain.model.valueobject.Email;
import com.alertabarrio.domain.port.out.EmailPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EnviarEmailUseCaseImpl")
class EnviarEmailUseCaseImplTest {

    @Mock
    private EmailPort emailPort;

    @InjectMocks
    private EnviarEmailUseCaseImpl useCase;

    @Test
    @DisplayName("con command válido envía email")
    void execute_conCommandValido_enviaEmail() {
        EnviarEmailCommand command = new EnviarEmailCommand("to@test.com", "Asunto", "Contenido");

        useCase.execute(command);

        verify(emailPort).enviar(any(Email.class), eq("Asunto"), eq("Contenido"));
    }

    @Test
    @DisplayName("con toEmail vacío lanza IllegalArgumentException")
    void execute_conToEmailVacio_lanzaExcepcion() {
        EnviarEmailCommand command = new EnviarEmailCommand("", "Asunto", "Contenido");

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(command));

        verify(emailPort, never()).enviar(any(), any(), any());
    }
}
