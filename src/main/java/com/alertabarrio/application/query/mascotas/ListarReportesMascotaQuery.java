package com.alertabarrio.application.query.mascotas;

import com.alertabarrio.domain.model.valueobject.Paginacion;

/**
 * Query del feed público de reportes de mascotas.
 * Filtros opcionales (null = no filtrar): estado, tipo de reporte, ciudad.
 */
public record ListarReportesMascotaQuery(
        Paginacion paginacion,
        String estado,
        String tipoReporte,
        Long ciudadId
) {
}
