package com.alertabarrio.domain.port.in.mascotas;

import com.alertabarrio.application.command.mascotas.ActualizarReporteMascotaCommand;
import com.alertabarrio.application.dto.mascotas.ReporteMascotaDTO;

public interface ActualizarReporteMascotaUseCase {
    ReporteMascotaDTO execute(ActualizarReporteMascotaCommand command);
}
