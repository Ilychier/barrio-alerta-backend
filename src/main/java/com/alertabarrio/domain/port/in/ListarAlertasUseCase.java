package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.query.ListarAlertasQuery;
import com.alertabarrio.application.dto.AlertaDTO;
import org.springframework.data.domain.Page;

public interface ListarAlertasUseCase {
    Page<AlertaDTO> execute(ListarAlertasQuery query);
}
