package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.dto.CuadranteDTO;
import com.alertabarrio.application.query.BuscarCuadranteQuery;

import java.util.Optional;

public interface BuscarCuadranteUseCase {
    Optional<CuadranteDTO> execute(BuscarCuadranteQuery query);
}
