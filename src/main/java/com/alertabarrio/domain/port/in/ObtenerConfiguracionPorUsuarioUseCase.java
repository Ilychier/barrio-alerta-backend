package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.dto.ConfiguracionDTO;
import com.alertabarrio.application.query.ObtenerConfiguracionPorUsuarioQuery;

public interface ObtenerConfiguracionPorUsuarioUseCase {
    ConfiguracionDTO execute(ObtenerConfiguracionPorUsuarioQuery query);
}
