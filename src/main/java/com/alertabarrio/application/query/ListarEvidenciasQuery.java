package com.alertabarrio.application.query;

import org.springframework.data.domain.Pageable;

public record ListarEvidenciasQuery(Long alertaId, Pageable pageable) {
}
