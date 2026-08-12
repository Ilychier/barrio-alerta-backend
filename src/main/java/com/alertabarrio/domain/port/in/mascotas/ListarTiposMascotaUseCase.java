package com.alertabarrio.domain.port.in.mascotas;

import com.alertabarrio.application.dto.mascotas.TipoMascotaDTO;

import java.util.List;

public interface ListarTiposMascotaUseCase {
    List<TipoMascotaDTO> execute();
}
