package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.dto.CategoriaDTO;
import com.alertabarrio.application.query.ListarCategoriasQuery;
import org.springframework.data.domain.Page;

public interface ListarCategoriasUseCase {
    Page<CategoriaDTO> execute(ListarCategoriasQuery query);
}
