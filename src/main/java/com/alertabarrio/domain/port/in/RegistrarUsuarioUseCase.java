package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.command.RegistrarUsuarioCommand;
import com.alertabarrio.application.dto.UsuarioDTO;

public interface RegistrarUsuarioUseCase {
    UsuarioDTO execute(RegistrarUsuarioCommand command);
}
