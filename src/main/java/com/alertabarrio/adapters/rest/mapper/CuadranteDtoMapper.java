package com.alertabarrio.adapters.rest.mapper;

import com.alertabarrio.adapters.rest.dto.CuadranteRequestDTO;
import com.alertabarrio.adapters.rest.dto.CuadranteResponseDTO;
import com.alertabarrio.application.command.*;
import com.alertabarrio.application.dto.CuadranteDTO;
import com.alertabarrio.application.query.BuscarCuadranteQuery;
import com.alertabarrio.application.query.ListarCuadrantesQuery;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.model.valueobject.Paginacion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

@Mapper(componentModel = "spring")
public interface CuadranteDtoMapper {

    CrearCuadranteCommand toCrearCommand(CuadranteRequestDTO dto);

    ActualizarCuadranteCommand toActualizarCommand(Long id, CuadranteRequestDTO dto);

    ParchearCuadranteCommand toParchearCommand(Long id, CuadranteRequestDTO dto);

    EliminarCuadranteCommand toEliminarCommand(Long id);

    BuscarCuadranteQuery toBuscarQuery(Long id);

    ListarCuadrantesQuery toListarQuery(Paginacion paginacion);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombreUnidad", source = "nombreUnidad")
    @Mapping(target = "telefonoEmergencia", source = "telefonoEmergencia")
    CuadranteResponseDTO toResponse(CuadranteDTO dto);

    default Page<CuadranteResponseDTO> toResponsePage(Pagina<CuadranteDTO> pagina) {
        return new PageImpl<>(
                pagina.contenido().stream().map(this::toResponse).toList(),
                org.springframework.data.domain.PageRequest.of(pagina.pagina(), pagina.tamanio()),
                pagina.totalElementos()
        );
    }
}
