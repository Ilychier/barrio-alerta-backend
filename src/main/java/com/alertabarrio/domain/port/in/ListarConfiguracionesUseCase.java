package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.query.ListarConfiguracionesQuery;
import com.alertabarrio.application.dto.ConfiguracionDTO;
import org.springframework.data.domain.Page;

public interface ListarConfiguracionesUseCase {
    Page<ConfiguracionDTO> execute(ListarConfiguracionesQuery query);
}
