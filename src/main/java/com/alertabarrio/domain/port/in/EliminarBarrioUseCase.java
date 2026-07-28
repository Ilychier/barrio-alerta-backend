package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.command.EliminarBarrioCommand;

public interface EliminarBarrioUseCase {
    void execute(EliminarBarrioCommand command);
}
