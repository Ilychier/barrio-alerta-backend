package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.query.BuscarBarrioQuery;
import com.alertabarrio.application.dto.BarrioDTO;

public interface BuscarBarrioUseCase {
    BarrioDTO execute(BuscarBarrioQuery query);
}
