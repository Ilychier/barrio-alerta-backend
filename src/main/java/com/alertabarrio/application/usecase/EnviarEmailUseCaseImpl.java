package com.alertabarrio.application.usecase;

import com.alertabarrio.application.UseCase;
import com.alertabarrio.application.command.EnviarEmailCommand;
import com.alertabarrio.domain.model.valueobject.Email;
import com.alertabarrio.domain.port.in.EnviarEmailUseCase;
import com.alertabarrio.domain.port.out.EmailPort;

@UseCase
public class EnviarEmailUseCaseImpl implements EnviarEmailUseCase {

    private final EmailPort emailPort;

    public EnviarEmailUseCaseImpl(EmailPort emailPort) {
        this.emailPort = emailPort;
    }

    @Override
    public void execute(EnviarEmailCommand command) {
        if (command.toEmail() == null || command.toEmail().isBlank()) {
            throw new IllegalArgumentException("El destinatario no puede estar vacío");
        }
        emailPort.enviar(new Email(command.toEmail()), command.asunto(), command.contenido());
    }
}
