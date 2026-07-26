package com.alertabarrio.adapters.rest.mapper;

import com.alertabarrio.adapters.rest.dto.CuadranteRequestDTO;
import com.alertabarrio.adapters.rest.dto.CuadranteResponseDTO;
import com.alertabarrio.application.command.*;
import com.alertabarrio.application.dto.CuadranteDTO;
import com.alertabarrio.application.query.BuscarCuadranteQuery;
import com.alertabarrio.application.query.ListarCuadrantesQuery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface CuadranteDtoMapper {

    CrearCuadranteCommand toCrearCommand(CuadranteRequestDTO dto);

    ActualizarCuadranteCommand toActualizarCommand(Long id, CuadranteRequestDTO dto);

    ParchearCuadranteCommand toParchearCommand(Long id, CuadranteRequestDTO dto);

    EliminarCuadranteCommand toEliminarCommand(Long id);

    BuscarCuadranteQuery toBuscarQuery(Long id);

    ListarCuadrantesQuery toListarQuery(org.springframework.data.domain.Pageable pageable);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombreUnidad", source = "nombreUnidad")
    @Mapping(target = "telefonoEmergencia", source = "telefonoEmergencia")
    CuadranteResponseDTO toResponse(CuadranteDTO dto);

    default Page<CuadranteResponseDTO> toResponsePage(Page<CuadranteDTO> page) {
        return page.map(this::toResponse);
    }
}
