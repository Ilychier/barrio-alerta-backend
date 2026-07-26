package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.dto.CategoriaDescripcionDTO;
import com.alertabarrio.application.query.ListarCategoriaDescripcionesQuery;
import org.springframework.data.domain.Page;

public interface ListarCategoriaDescripcionesUseCase {
    Page<CategoriaDescripcionDTO> execute(ListarCategoriaDescripcionesQuery query);
}
