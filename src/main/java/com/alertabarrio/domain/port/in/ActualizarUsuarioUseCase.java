package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.command.ActualizarUsuarioCommand;
import com.alertabarrio.application.dto.UsuarioDTO;

public interface ActualizarUsuarioUseCase {
    UsuarioDTO execute(ActualizarUsuarioCommand command);
}
