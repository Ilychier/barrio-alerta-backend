package com.alertabarrio.domain.port.in.mascotas;

import com.alertabarrio.application.dto.mascotas.ReporteMascotaDTO;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.model.valueobject.Paginacion;

/**
 * Historias de rescate: reportes cuyo estado es RESCUED.
 */
public interface ListarReportesRescatadosUseCase {
    Pagina<ReporteMascotaDTO> execute(Paginacion paginacion);
}
