package com.alertabarrio.adapters.rest.mapper;

import com.alertabarrio.adapters.rest.dto.BarrioRequestDTO;
import com.alertabarrio.adapters.rest.dto.BarrioResponseDTO;
import com.alertabarrio.application.command.*;
import com.alertabarrio.application.dto.BarrioDTO;
import com.alertabarrio.application.query.BuscarBarrioQuery;
import com.alertabarrio.application.query.ListarBarriosQuery;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.model.valueobject.Paginacion;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

@Mapper(componentModel = "spring")
public interface BarrioDtoMapper {

    CrearBarrioCommand toCrearCommand(BarrioRequestDTO dto);

    ActualizarBarrioCommand toActualizarCommand(Long id, BarrioRequestDTO dto);

    ParchearBarrioCommand toParchearCommand(Long id, BarrioRequestDTO dto);

    EliminarBarrioCommand toEliminarCommand(Long id);

    BuscarBarrioQuery toBuscarQuery(Long id);

    ListarBarriosQuery toListarQuery(Paginacion paginacion);

    default ListarBarriosQuery toListarQuery(Long localidadId, Paginacion paginacion) {
        return new ListarBarriosQuery(paginacion, localidadId);
    }

    BarrioResponseDTO toResponse(BarrioDTO dto);

    default Page<BarrioResponseDTO> toResponsePage(Pagina<BarrioDTO> pagina) {
        return new PageImpl<>(
                pagina.contenido().stream().map(this::toResponse).toList(),
                org.springframework.data.domain.PageRequest.of(pagina.pagina(), pagina.tamanio()),
                pagina.totalElementos()
        );
    }
}
