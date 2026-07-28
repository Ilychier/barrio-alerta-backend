package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.command.EliminarUsuarioCommand;

public interface EliminarUsuarioUseCase {
    void execute(EliminarUsuarioCommand command);
}
