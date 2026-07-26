package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.command.ActualizarEvidenciaCommand;
import com.alertabarrio.application.dto.EvidenciaDTO;

public interface ActualizarEvidenciaUseCase {
    EvidenciaDTO execute(ActualizarEvidenciaCommand command);
}
