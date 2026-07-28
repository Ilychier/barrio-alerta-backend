package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.query.ListarBarriosQuery;
import com.alertabarrio.application.dto.BarrioDTO;
import com.alertabarrio.domain.model.valueobject.Pagina;

public interface ListarBarriosUseCase {
    Pagina<BarrioDTO> execute(ListarBarriosQuery query);
}
