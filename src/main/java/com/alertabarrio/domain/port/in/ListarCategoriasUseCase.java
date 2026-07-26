package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.dto.CategoriaDTO;
import com.alertabarrio.application.query.ListarCategoriasQuery;
import com.alertabarrio.domain.model.valueobject.Pagina;

public interface ListarCategoriasUseCase {
    Pagina<CategoriaDTO> execute(ListarCategoriasQuery query);
}
