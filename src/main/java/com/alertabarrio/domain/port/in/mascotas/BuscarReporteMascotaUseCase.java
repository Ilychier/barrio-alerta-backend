package com.alertabarrio.domain.port.in.mascotas;

import com.alertabarrio.application.dto.mascotas.ReporteMascotaDTO;
import com.alertabarrio.application.query.mascotas.BuscarReporteMascotaQuery;

public interface BuscarReporteMascotaUseCase {
    ReporteMascotaDTO execute(BuscarReporteMascotaQuery query);
}
