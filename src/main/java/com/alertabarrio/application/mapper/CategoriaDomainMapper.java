package com.alertabarrio.application.mapper;

import com.alertabarrio.application.dto.CategoriaDTO;
import com.alertabarrio.domain.model.Categoria;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoriaDomainMapper {

    @Mapping(target = "id", source = "id.value")
    CategoriaDTO toDto(Categoria categoria);
}
