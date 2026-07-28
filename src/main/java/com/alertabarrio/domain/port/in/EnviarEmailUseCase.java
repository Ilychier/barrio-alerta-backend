package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.command.EnviarEmailCommand;

public interface EnviarEmailUseCase {
    void execute(EnviarEmailCommand command);
}
