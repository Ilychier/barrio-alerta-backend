package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.query.BuscarEvidenciaQuery;
import com.alertabarrio.application.dto.EvidenciaDTO;

public interface BuscarEvidenciaUseCase {
    EvidenciaDTO execute(BuscarEvidenciaQuery query);
}
