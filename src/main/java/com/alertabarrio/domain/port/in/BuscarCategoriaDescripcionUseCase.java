package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.dto.CategoriaDescripcionDTO;
import com.alertabarrio.application.query.BuscarCategoriaDescripcionQuery;

import java.util.Optional;

public interface BuscarCategoriaDescripcionUseCase {
    Optional<CategoriaDescripcionDTO> execute(BuscarCategoriaDescripcionQuery query);
}
