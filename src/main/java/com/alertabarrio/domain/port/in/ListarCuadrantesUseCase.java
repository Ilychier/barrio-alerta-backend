package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.dto.CuadranteDTO;
import com.alertabarrio.application.query.ListarCuadrantesQuery;
import org.springframework.data.domain.Page;

public interface ListarCuadrantesUseCase {
    Page<CuadranteDTO> execute(ListarCuadrantesQuery query);
}
