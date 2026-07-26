package com.alertabarrio.application.mapper;

import com.alertabarrio.application.dto.CuadranteDTO;
import com.alertabarrio.domain.model.Cuadrante;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CuadranteDomainMapper {

    @Mapping(target = "id", source = "id.value")
    CuadranteDTO toDto(Cuadrante cuadrante);
}
