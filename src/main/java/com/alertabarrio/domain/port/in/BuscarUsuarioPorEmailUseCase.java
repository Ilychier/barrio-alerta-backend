package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.dto.UsuarioDTO;
import com.alertabarrio.application.query.BuscarUsuarioPorEmailQuery;

public interface BuscarUsuarioPorEmailUseCase {
    UsuarioDTO execute(BuscarUsuarioPorEmailQuery query);
}
