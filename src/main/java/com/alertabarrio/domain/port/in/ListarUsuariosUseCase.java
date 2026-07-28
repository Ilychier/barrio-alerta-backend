package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.query.ListarUsuariosQuery;
import com.alertabarrio.application.dto.UsuarioDTO;
import com.alertabarrio.domain.model.valueobject.Pagina;

public interface ListarUsuariosUseCase {
    Pagina<UsuarioDTO> execute(ListarUsuariosQuery query);
}
