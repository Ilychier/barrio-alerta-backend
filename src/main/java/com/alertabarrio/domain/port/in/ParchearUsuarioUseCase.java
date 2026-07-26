package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.command.ParchearUsuarioCommand;
import com.alertabarrio.application.dto.UsuarioDTO;

public interface ParchearUsuarioUseCase {
    UsuarioDTO execute(ParchearUsuarioCommand command);
}
