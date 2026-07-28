package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.query.ListarConfiguracionesQuery;
import com.alertabarrio.application.dto.ConfiguracionDTO;
import com.alertabarrio.domain.model.valueobject.Pagina;

public interface ListarConfiguracionesUseCase {
    Pagina<ConfiguracionDTO> execute(ListarConfiguracionesQuery query);
}
