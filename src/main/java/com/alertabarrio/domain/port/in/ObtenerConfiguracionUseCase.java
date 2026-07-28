package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.query.ObtenerConfiguracionQuery;
import com.alertabarrio.application.dto.ConfiguracionDTO;

public interface ObtenerConfiguracionUseCase {
    ConfiguracionDTO execute(ObtenerConfiguracionQuery query);
}
