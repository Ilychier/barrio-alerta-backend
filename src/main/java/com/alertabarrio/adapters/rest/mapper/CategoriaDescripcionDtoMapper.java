package com.alertabarrio.adapters.rest.mapper;

import com.alertabarrio.adapters.rest.dto.CategoriaDescripcionRequestDTO;
import com.alertabarrio.adapters.rest.dto.CategoriaDescripcionResponseDTO;
import com.alertabarrio.application.command.*;
import com.alertabarrio.application.dto.CategoriaDescripcionDTO;
import com.alertabarrio.application.query.BuscarCategoriaDescripcionQuery;
import com.alertabarrio.application.query.ListarCategoriaDescripcionesQuery;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.model.valueobject.Paginacion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

@Mapper(componentModel = "spring")
public interface CategoriaDescripcionDtoMapper {

    CrearCategoriaDescripcionCommand toCrearCommand(CategoriaDescripcionRequestDTO dto);

    ActualizarCategoriaDescripcionCommand toActualizarCommand(Long id, CategoriaDescripcionRequestDTO dto);

    ParchearCategoriaDescripcionCommand toParchearCommand(Long id, CategoriaDescripcionRequestDTO dto);

    EliminarCategoriaDescripcionCommand toEliminarCommand(Long id);

    BuscarCategoriaDescripcionQuery toBuscarQuery(Long id);

    ListarCategoriaDescripcionesQuery toListarQuery(Long categoriaId, Paginacion paginacion);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "descripcion", source = "descripcion")
    @Mapping(target = "categoriaId", source = "categoriaId")
    @Mapping(target = "imagenUrl", source = "imagenUrl")
    CategoriaDescripcionResponseDTO toResponse(CategoriaDescripcionDTO dto);

    default Page<CategoriaDescripcionResponseDTO> toResponsePage(Pagina<CategoriaDescripcionDTO> pagina) {
        return new PageImpl<>(
                pagina.contenido().stream().map(this::toResponse).toList(),
                org.springframework.data.domain.PageRequest.of(pagina.pagina(), pagina.tamanio()),
                pagina.totalElementos()
        );
    }
}
