package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.command.EliminarCategoriaDescripcionCommand;

public interface EliminarCategoriaDescripcionUseCase {
    void execute(EliminarCategoriaDescripcionCommand command);
}
