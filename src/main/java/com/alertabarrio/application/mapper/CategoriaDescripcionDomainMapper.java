package com.alertabarrio.application.mapper;

import com.alertabarrio.application.dto.CategoriaDescripcionDTO;
import com.alertabarrio.domain.model.CategoriaDescripcion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoriaDescripcionDomainMapper {

    @Mapping(target = "id", source = "id.value")
    @Mapping(target = "categoriaId", source = "categoriaId.value")
    CategoriaDescripcionDTO toDto(CategoriaDescripcion categoriaDescripcion);
}
