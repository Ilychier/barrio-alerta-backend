package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.command.EliminarEvidenciaCommand;

public interface EliminarEvidenciaUseCase {
    void execute(EliminarEvidenciaCommand command);
}
