package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.command.CrearEvidenciaCommand;
import com.alertabarrio.application.dto.EvidenciaDTO;

public interface CrearEvidenciaUseCase {
    EvidenciaDTO execute(CrearEvidenciaCommand command);
}
