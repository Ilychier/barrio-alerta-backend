package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.query.BuscarUsuarioQuery;
import com.alertabarrio.application.dto.UsuarioDTO;

public interface BuscarUsuarioUseCase {
    UsuarioDTO execute(BuscarUsuarioQuery query);
}
