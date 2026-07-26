package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.command.ParchearEvidenciaCommand;
import com.alertabarrio.application.dto.EvidenciaDTO;

public interface ParchearEvidenciaUseCase {
    EvidenciaDTO execute(ParchearEvidenciaCommand command);
}
