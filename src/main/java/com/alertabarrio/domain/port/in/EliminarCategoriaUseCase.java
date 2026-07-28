package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.command.EliminarCategoriaCommand;

public interface EliminarCategoriaUseCase {
    void execute(EliminarCategoriaCommand command);
}
