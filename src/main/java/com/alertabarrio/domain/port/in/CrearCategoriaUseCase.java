package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.command.CrearCategoriaCommand;
import com.alertabarrio.application.dto.CategoriaDTO;

public interface CrearCategoriaUseCase {
    CategoriaDTO execute(CrearCategoriaCommand command);
}
