package com.alertabarrio.application.mapper.mascotas;

import com.alertabarrio.application.dto.mascotas.CiudadDTO;
import com.alertabarrio.domain.model.mascotas.Ciudad;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CiudadDomainMapper {

    @Mapping(target = "id", source = "id.value")
    CiudadDTO toDto(Ciudad ciudad);}
