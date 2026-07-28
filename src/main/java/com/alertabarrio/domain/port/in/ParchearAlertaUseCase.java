package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.command.ParchearAlertaCommand;
import com.alertabarrio.application.dto.AlertaDTO;

public interface ParchearAlertaUseCase {
    AlertaDTO execute(ParchearAlertaCommand command);
}
