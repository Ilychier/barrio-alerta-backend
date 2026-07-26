package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.query.ListarEvidenciasQuery;
import com.alertabarrio.application.dto.EvidenciaDTO;
import org.springframework.data.domain.Page;

public interface ListarEvidenciasUseCase {
    Page<EvidenciaDTO> execute(ListarEvidenciasQuery query);
}
