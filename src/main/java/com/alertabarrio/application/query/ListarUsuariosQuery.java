package com.alertabarrio.application.query;

import org.springframework.data.domain.Pageable;

public record ListarUsuariosQuery(Pageable pageable) {
}
