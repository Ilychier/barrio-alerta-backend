package com.alertabarrio.application.mapper;

import com.alertabarrio.application.dto.ConfiguracionDTO;
import com.alertabarrio.domain.model.Configuracion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ConfiguracionDomainMapper {

    @Mapping(target = "id", source = "id.value")
    @Mapping(target = "usuarioId", source = "usuarioId.value")
    ConfiguracionDTO toDto(Configuracion configuracion);
}
