package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.command.ActualizarCuadranteCommand;
import com.alertabarrio.application.dto.CuadranteDTO;

public interface ActualizarCuadranteUseCase {
    CuadranteDTO execute(ActualizarCuadranteCommand command);
}
