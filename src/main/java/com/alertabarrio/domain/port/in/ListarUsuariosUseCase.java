package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.query.ListarUsuariosQuery;
import com.alertabarrio.application.dto.UsuarioDTO;
import org.springframework.data.domain.Page;

public interface ListarUsuariosUseCase {
    Page<UsuarioDTO> execute(ListarUsuariosQuery query);
}
