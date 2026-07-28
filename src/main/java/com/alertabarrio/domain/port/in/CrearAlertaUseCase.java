package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.command.CrearAlertaCommand;
import com.alertabarrio.application.dto.AlertaDTO;

public interface CrearAlertaUseCase {
    AlertaDTO execute(CrearAlertaCommand command);
}
