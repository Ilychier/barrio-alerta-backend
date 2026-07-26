package com.alertabarrio.adapters.rest.mapper;

import com.alertabarrio.adapters.rest.dto.CategoriaDescripcionRequestDTO;
import com.alertabarrio.adapters.rest.dto.CategoriaDescripcionResponseDTO;
import com.alertabarrio.application.command.*;
import com.alertabarrio.application.dto.CategoriaDescripcionDTO;
import com.alertabarrio.application.query.BuscarCategoriaDescripcionQuery;
import com.alertabarrio.application.query.ListarCategoriaDescripcionesQuery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface CategoriaDescripcionDtoMapper {

    CrearCategoriaDescripcionCommand toCrearCommand(CategoriaDescripcionRequestDTO dto);

    ActualizarCategoriaDescripcionCommand toActualizarCommand(Long id, CategoriaDescripcionRequestDTO dto);

    ParchearCategoriaDescripcionCommand toParchearCommand(Long id, CategoriaDescripcionRequestDTO dto);

    EliminarCategoriaDescripcionCommand toEliminarCommand(Long id);

    BuscarCategoriaDescripcionQuery toBuscarQuery(Long id);

    ListarCategoriaDescripcionesQuery toListarQuery(Long categoriaId, org.springframework.data.domain.Pageable pageable);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "descripcion", source = "descripcion")
    @Mapping(target = "categoriaId", source = "categoriaId")
    @Mapping(target = "imagenUrl", source = "imagenUrl")
    CategoriaDescripcionResponseDTO toResponse(CategoriaDescripcionDTO dto);

    default Page<CategoriaDescripcionResponseDTO> toResponsePage(Page<CategoriaDescripcionDTO> page) {
        return page.map(this::toResponse);
    }
}
