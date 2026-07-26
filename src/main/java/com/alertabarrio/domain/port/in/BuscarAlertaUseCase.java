package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.query.BuscarAlertaQuery;
import com.alertabarrio.application.dto.AlertaDTO;

public interface BuscarAlertaUseCase {
    AlertaDTO execute(BuscarAlertaQuery query);
}
