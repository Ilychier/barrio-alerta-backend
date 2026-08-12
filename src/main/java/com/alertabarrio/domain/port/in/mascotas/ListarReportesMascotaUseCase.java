package com.alertabarrio.domain.port.in.mascotas;

import com.alertabarrio.application.dto.mascotas.ReporteMascotaDTO;
import com.alertabarrio.application.query.mascotas.ListarReportesMascotaQuery;
import com.alertabarrio.domain.model.valueobject.Pagina;

public interface ListarReportesMascotaUseCase {
    Pagina<ReporteMascotaDTO> execute(ListarReportesMascotaQuery query);
}
