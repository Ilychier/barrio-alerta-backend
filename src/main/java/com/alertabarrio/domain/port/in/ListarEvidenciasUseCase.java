package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.query.ListarEvidenciasQuery;
import com.alertabarrio.application.dto.EvidenciaDTO;
import com.alertabarrio.domain.model.valueobject.Pagina;

public interface ListarEvidenciasUseCase {
    Pagina<EvidenciaDTO> execute(ListarEvidenciasQuery query);
}
