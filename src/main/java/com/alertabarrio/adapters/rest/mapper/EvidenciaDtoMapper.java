package com.alertabarrio.adapters.rest.mapper;

import com.alertabarrio.adapters.rest.dto.EvidenciaRequestDTO;
import com.alertabarrio.adapters.rest.dto.EvidenciaResponseDTO;
import com.alertabarrio.application.command.*;
import com.alertabarrio.application.dto.EvidenciaDTO;
import com.alertabarrio.application.query.BuscarEvidenciaQuery;
import com.alertabarrio.application.query.ListarEvidenciasQuery;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.model.valueobject.Paginacion;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

@Mapper(componentModel = "spring")
public interface EvidenciaDtoMapper {

    CrearEvidenciaCommand toCrearCommand(EvidenciaRequestDTO dto);

    ActualizarEvidenciaCommand toActualizarCommand(Long id, EvidenciaRequestDTO dto);

    ParchearEvidenciaCommand toParchearCommand(Long id, EvidenciaRequestDTO dto);

    EliminarEvidenciaCommand toEliminarCommand(Long id);

    BuscarEvidenciaQuery toBuscarQuery(Long id);

    ListarEvidenciasQuery toListarQuery(Long alertaId, Paginacion paginacion);

    EvidenciaResponseDTO toResponse(EvidenciaDTO dto);

    default Page<EvidenciaResponseDTO> toResponsePage(Pagina<EvidenciaDTO> pagina) {
        return new PageImpl<>(
                pagina.contenido().stream().map(this::toResponse).toList(),
                org.springframework.data.domain.PageRequest.of(pagina.pagina(), pagina.tamanio()),
                pagina.totalElementos()
        );
    }
}
