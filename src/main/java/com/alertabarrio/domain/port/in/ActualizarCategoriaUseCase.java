package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.command.ActualizarCategoriaCommand;
import com.alertabarrio.application.dto.CategoriaDTO;

public interface ActualizarCategoriaUseCase {
    CategoriaDTO execute(ActualizarCategoriaCommand command);
}
