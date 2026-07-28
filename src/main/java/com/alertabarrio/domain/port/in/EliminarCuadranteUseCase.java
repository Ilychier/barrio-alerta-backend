package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.command.EliminarCuadranteCommand;

public interface EliminarCuadranteUseCase {
    void execute(EliminarCuadranteCommand command);
}
