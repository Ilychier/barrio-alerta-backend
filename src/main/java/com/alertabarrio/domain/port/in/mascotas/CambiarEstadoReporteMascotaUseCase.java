package com.alertabarrio.domain.port.in.mascotas;

import com.alertabarrio.application.command.mascotas.CambiarEstadoReporteMascotaCommand;
import com.alertabarrio.application.dto.mascotas.ReporteMascotaDTO;

public interface CambiarEstadoReporteMascotaUseCase {
    ReporteMascotaDTO execute(CambiarEstadoReporteMascotaCommand command);
}
