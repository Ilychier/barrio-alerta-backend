package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.query.ListarBarriosQuery;
import com.alertabarrio.application.dto.BarrioDTO;
import org.springframework.data.domain.Page;

public interface ListarBarriosUseCase {
    Page<BarrioDTO> execute(ListarBarriosQuery query);
}
