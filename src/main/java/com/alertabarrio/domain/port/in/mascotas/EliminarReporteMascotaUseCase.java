package com.alertabarrio.domain.port.in.mascotas;

import com.alertabarrio.application.command.mascotas.EliminarReporteMascotaCommand;

public interface EliminarReporteMascotaUseCase {
    void execute(EliminarReporteMascotaCommand command);
}
