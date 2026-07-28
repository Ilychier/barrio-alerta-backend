package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.command.ActualizarAlertaCommand;
import com.alertabarrio.application.dto.AlertaDTO;

public interface ActualizarAlertaUseCase {
    AlertaDTO execute(ActualizarAlertaCommand command);
}
