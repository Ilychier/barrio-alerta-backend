package com.alertabarrio.application.query;

import com.alertabarrio.domain.model.valueobject.Paginacion;
import java.time.LocalDate;

public record ListarAlertasQuery(Paginacion paginacion, LocalDate fecha, Long barrioId) {
}
