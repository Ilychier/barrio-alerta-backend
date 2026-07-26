package com.alertabarrio.adapters.rest.mapper;

import com.alertabarrio.adapters.rest.dto.CategoriaRequestDTO;
import com.alertabarrio.adapters.rest.dto.CategoriaResponseDTO;
import com.alertabarrio.application.command.*;
import com.alertabarrio.application.dto.CategoriaDTO;
import com.alertabarrio.application.query.BuscarCategoriaQuery;
import com.alertabarrio.application.query.ListarCategoriasQuery;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.model.valueobject.Paginacion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

@Mapper(componentModel = "spring")
public interface CategoriaDtoMapper {

    CrearCategoriaCommand toCrearCommand(CategoriaRequestDTO dto);

    ActualizarCategoriaCommand toActualizarCommand(Long id, CategoriaRequestDTO dto);

    ParchearCategoriaCommand toParchearCommand(Long id, CategoriaRequestDTO dto);

    EliminarCategoriaCommand toEliminarCommand(Long id);

    BuscarCategoriaQuery toBuscarQuery(Long id);

    ListarCategoriasQuery toListarQuery(Paginacion paginacion);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    @Mapping(target = "iconoReferencia", source = "iconoReferencia")
    CategoriaResponseDTO toResponse(CategoriaDTO dto);

    default Page<CategoriaResponseDTO> toResponsePage(Pagina<CategoriaDTO> pagina) {
        return new PageImpl<>(
                pagina.contenido().stream().map(this::toResponse).toList(),
                org.springframework.data.domain.PageRequest.of(pagina.pagina(), pagina.tamanio()),
                pagina.totalElementos()
        );
    }
}
