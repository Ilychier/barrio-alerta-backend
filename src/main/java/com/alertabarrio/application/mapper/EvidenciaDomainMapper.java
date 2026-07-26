package com.alertabarrio.application.mapper;

import com.alertabarrio.application.dto.EvidenciaDTO;
import com.alertabarrio.domain.model.Evidencia;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EvidenciaDomainMapper {

    @Mapping(target = "id", source = "id.value")
    @Mapping(target = "alertaId", source = "alertaId.value")
    EvidenciaDTO toDto(Evidencia evidencia);
}
