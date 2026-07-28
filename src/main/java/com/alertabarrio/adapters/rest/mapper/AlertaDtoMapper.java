package com.alertabarrio.adapters.rest.mapper;

import com.alertabarrio.adapters.rest.dto.AlertaRequestDTO;
import com.alertabarrio.adapters.rest.dto.AlertaResponseDTO;
import com.alertabarrio.application.command.*;
import com.alertabarrio.application.dto.AlertaDTO;
import com.alertabarrio.application.query.BuscarAlertaQuery;
import com.alertabarrio.application.query.ListarAlertasQuery;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.model.valueobject.Paginacion;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

@Mapper(componentModel = "spring")
public interface AlertaDtoMapper {

    CrearAlertaCommand toCrearCommand(AlertaRequestDTO dto);

    ActualizarAlertaCommand toActualizarCommand(Long id, AlertaRequestDTO dto);

    ParchearAlertaCommand toParchearCommand(Long id, AlertaRequestDTO dto);

    EliminarAlertaCommand toEliminarCommand(Long id);

    BuscarAlertaQuery toBuscarQuery(Long id);

    ListarAlertasQuery toListarQuery(Paginacion paginacion, java.time.LocalDate fecha, Long barrioId);

    AlertaResponseDTO toResponse(AlertaDTO dto);

    default Page<AlertaResponseDTO> toResponsePage(Pagina<AlertaDTO> pagina) {
        return new PageImpl<>(
                pagina.contenido().stream().map(this::toResponse).toList(),
                org.springframework.data.domain.PageRequest.of(pagina.pagina(), pagina.tamanio()),
                pagina.totalElementos()
        );
    }
}
