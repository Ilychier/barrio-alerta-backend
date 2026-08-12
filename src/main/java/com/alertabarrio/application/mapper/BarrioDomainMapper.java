package com.alertabarrio.application.mapper;

import com.alertabarrio.application.dto.BarrioDTO;
import com.alertabarrio.domain.model.Barrio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BarrioDomainMapper {

    @Mapping(target = "id", source = "id.value")
    @Mapping(target = "cuadranteId", source = "cuadranteId.value")
    @Mapping(target = "ciudadId", source = "ciudadId.value")
    BarrioDTO toDto(Barrio barrio);
}
