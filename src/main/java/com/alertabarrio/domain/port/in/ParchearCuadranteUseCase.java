package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.command.ParchearCuadranteCommand;
import com.alertabarrio.application.dto.CuadranteDTO;

public interface ParchearCuadranteUseCase {
    CuadranteDTO execute(ParchearCuadranteCommand command);
}
