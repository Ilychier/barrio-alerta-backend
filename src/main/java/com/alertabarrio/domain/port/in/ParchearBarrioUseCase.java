package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.command.ParchearBarrioCommand;
import com.alertabarrio.application.dto.BarrioDTO;

public interface ParchearBarrioUseCase {
    BarrioDTO execute(ParchearBarrioCommand command);
}
