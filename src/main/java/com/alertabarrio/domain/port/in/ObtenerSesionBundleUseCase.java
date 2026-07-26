package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.dto.SesionDTO;

public interface ObtenerSesionBundleUseCase {
    SesionDTO execute(String email);
}
