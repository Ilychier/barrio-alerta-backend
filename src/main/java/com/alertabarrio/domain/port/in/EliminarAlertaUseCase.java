package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.command.EliminarAlertaCommand;

public interface EliminarAlertaUseCase {
    void execute(EliminarAlertaCommand command);
}
