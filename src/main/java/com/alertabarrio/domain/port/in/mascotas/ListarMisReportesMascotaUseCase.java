package com.alertabarrio.domain.port.in.mascotas;

import com.alertabarrio.application.dto.mascotas.ReporteMascotaDTO;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.model.valueobject.Paginacion;

/**
 * Mis reportes: reportes creados por un usuario (incluye DELETED).
 */
public interface ListarMisReportesMascotaUseCase {
    Pagina<ReporteMascotaDTO> execute(Long usuarioId, Paginacion paginacion);
}
