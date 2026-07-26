package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.query.ListarAlertasQuery;
import com.alertabarrio.application.dto.AlertaDTO;
import com.alertabarrio.domain.model.valueobject.Pagina;

public interface ListarAlertasUseCase {
    Pagina<AlertaDTO> execute(ListarAlertasQuery query);
}
