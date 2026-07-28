package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.command.CrearCategoriaDescripcionCommand;
import com.alertabarrio.application.dto.CategoriaDescripcionDTO;

public interface CrearCategoriaDescripcionUseCase {
    CategoriaDescripcionDTO execute(CrearCategoriaDescripcionCommand command);
}
