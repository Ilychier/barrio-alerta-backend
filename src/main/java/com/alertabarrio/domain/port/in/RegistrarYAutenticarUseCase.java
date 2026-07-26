package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.command.RegistrarUsuarioCommand;
import com.alertabarrio.application.dto.AuthResultDTO;

public interface RegistrarYAutenticarUseCase {
    AuthResultDTO execute(RegistrarUsuarioCommand command);
}
