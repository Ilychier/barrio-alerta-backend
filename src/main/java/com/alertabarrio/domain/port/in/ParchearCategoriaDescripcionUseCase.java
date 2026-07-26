package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.command.ParchearCategoriaDescripcionCommand;
import com.alertabarrio.application.dto.CategoriaDescripcionDTO;

public interface ParchearCategoriaDescripcionUseCase {
    CategoriaDescripcionDTO execute(ParchearCategoriaDescripcionCommand command);
}
