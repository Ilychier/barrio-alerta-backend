package com.alertabarrio.application.mapper;

import com.alertabarrio.application.dto.AlertaDTO;
import com.alertabarrio.domain.model.Alerta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AlertaDomainMapper {

    @Mapping(target = "id", source = "id.value")
    @Mapping(target = "usuarioId", source = "usuarioId.value")
    @Mapping(target = "categoriaId", source = "categoriaId.value")
    AlertaDTO toDto(Alerta alerta);
}
