package com.alertabarrio.adapters.rest.mapper.mascotas;

import com.alertabarrio.adapters.rest.dto.mascotas.TipoMascotaResponseDTO;
import com.alertabarrio.application.dto.mascotas.TipoMascotaDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TipoMascotaDtoMapper {

    TipoMascotaResponseDTO toResponse(TipoMascotaDTO dto);

    default List<TipoMascotaResponseDTO> toResponseList(List<TipoMascotaDTO> dtos) {
        return dtos.stream().map(this::toResponse).toList();
    }
}
