package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.command.ParchearCategoriaCommand;
import com.alertabarrio.application.dto.CategoriaDTO;

public interface ParchearCategoriaUseCase {
    CategoriaDTO execute(ParchearCategoriaCommand command);
}
