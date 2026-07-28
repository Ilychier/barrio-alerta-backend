package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.command.CrearCuadranteCommand;
import com.alertabarrio.application.dto.CuadranteDTO;

public interface CrearCuadranteUseCase {
    CuadranteDTO execute(CrearCuadranteCommand command);
}
