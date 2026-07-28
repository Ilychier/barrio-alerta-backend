package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.command.ActualizarCategoriaDescripcionCommand;
import com.alertabarrio.application.dto.CategoriaDescripcionDTO;

public interface ActualizarCategoriaDescripcionUseCase {
    CategoriaDescripcionDTO execute(ActualizarCategoriaDescripcionCommand command);
}
