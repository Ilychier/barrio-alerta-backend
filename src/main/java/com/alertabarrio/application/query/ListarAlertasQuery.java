package com.alertabarrio.application.query;

import org.springframework.data.domain.Pageable;
import java.time.LocalDate;

public record ListarAlertasQuery(Pageable pageable, LocalDate fecha, Long barrioId) {
}
