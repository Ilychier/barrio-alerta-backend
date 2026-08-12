package com.alertabarrio.domain.port.in.mascotas;

import com.alertabarrio.application.dto.mascotas.CiudadDTO;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.model.valueobject.Paginacion;

public interface ListarCiudadesUseCase {
    Pagina<CiudadDTO> execute(Paginacion paginacion);
}
