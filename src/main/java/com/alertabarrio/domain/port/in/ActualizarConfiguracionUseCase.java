package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.command.ActualizarConfiguracionCommand;
import com.alertabarrio.application.dto.ConfiguracionDTO;

public interface ActualizarConfiguracionUseCase {
    ConfiguracionDTO execute(ActualizarConfiguracionCommand command);
}
