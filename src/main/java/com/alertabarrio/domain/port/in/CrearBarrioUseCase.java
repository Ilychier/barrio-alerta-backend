package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.command.CrearBarrioCommand;
import com.alertabarrio.application.dto.BarrioDTO;

public interface CrearBarrioUseCase {
    BarrioDTO execute(CrearBarrioCommand command);
}
