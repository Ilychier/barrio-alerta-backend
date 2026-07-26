package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.dto.CategoriaDTO;
import com.alertabarrio.application.query.BuscarCategoriaQuery;

import java.util.Optional;

public interface BuscarCategoriaUseCase {
    Optional<CategoriaDTO> execute(BuscarCategoriaQuery query);
}
