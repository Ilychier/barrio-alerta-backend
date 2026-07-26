package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.command.CrearConfiguracionCommand;
import com.alertabarrio.application.dto.ConfiguracionDTO;

public interface CrearConfiguracionUseCase {
    ConfiguracionDTO execute(CrearConfiguracionCommand command);
}
