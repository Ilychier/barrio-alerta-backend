package com.alertabarrio.application.mapper;

import com.alertabarrio.application.dto.LocalidadDTO;
import com.alertabarrio.domain.model.Localidad;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LocalidadDomainMapper {

    @Mapping(target = "id", source = "id.value")
    LocalidadDTO toDto(Localidad localidad);
}
