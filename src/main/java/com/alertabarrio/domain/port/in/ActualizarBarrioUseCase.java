package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.command.ActualizarBarrioCommand;
import com.alertabarrio.application.dto.BarrioDTO;

public interface ActualizarBarrioUseCase {
    BarrioDTO execute(ActualizarBarrioCommand command);
}
