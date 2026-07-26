package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.dto.CategoriaDescripcionDTO;
import com.alertabarrio.application.query.ListarCategoriaDescripcionesQuery;
import com.alertabarrio.domain.model.valueobject.Pagina;

public interface ListarCategoriaDescripcionesUseCase {
    Pagina<CategoriaDescripcionDTO> execute(ListarCategoriaDescripcionesQuery query);
}
