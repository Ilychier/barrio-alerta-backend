package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.query.ListarLocalidadesQuery;
import com.alertabarrio.application.dto.LocalidadDTO;
import com.alertabarrio.domain.model.valueobject.Pagina;

public interface ListarLocalidadesUseCase {
    Pagina<LocalidadDTO> execute(ListarLocalidadesQuery query);
}
