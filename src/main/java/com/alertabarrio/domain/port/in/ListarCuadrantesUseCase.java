package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.dto.CuadranteDTO;
import com.alertabarrio.application.query.ListarCuadrantesQuery;
import com.alertabarrio.domain.model.valueobject.Pagina;

public interface ListarCuadrantesUseCase {
    Pagina<CuadranteDTO> execute(ListarCuadrantesQuery query);
}
