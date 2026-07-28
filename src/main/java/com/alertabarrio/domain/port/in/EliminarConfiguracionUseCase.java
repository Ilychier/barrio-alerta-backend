package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.command.EliminarConfiguracionCommand;

public interface EliminarConfiguracionUseCase {
    void execute(EliminarConfiguracionCommand command);
}
