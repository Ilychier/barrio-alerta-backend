package com.alertabarrio.domain.port.in.mascotas;

import com.alertabarrio.application.command.mascotas.CrearReporteMascotaCommand;
import com.alertabarrio.application.dto.mascotas.ReporteMascotaDTO;

public interface CrearReporteMascotaUseCase {
    ReporteMascotaDTO execute(CrearReporteMascotaCommand command);
}
