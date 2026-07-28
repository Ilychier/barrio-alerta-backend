package com.alertabarrio.adapters.rest.mapper;

import com.alertabarrio.adapters.rest.dto.ConfiguracionRequestDTO;
import com.alertabarrio.adapters.rest.dto.ConfiguracionResponseDTO;
import com.alertabarrio.application.command.*;
import com.alertabarrio.application.dto.ConfiguracionDTO;
import com.alertabarrio.application.query.ObtenerConfiguracionQuery;
import com.alertabarrio.application.query.ListarConfiguracionesQuery;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.model.valueobject.Paginacion;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

@Mapper(componentModel = "spring")
public interface ConfiguracionDtoMapper {

    CrearConfiguracionCommand toCrearCommand(ConfiguracionRequestDTO dto);

    ActualizarConfiguracionCommand toActualizarCommand(Long id, ConfiguracionRequestDTO dto);

    EliminarConfiguracionCommand toEliminarCommand(Long id);

    ObtenerConfiguracionQuery toObtenerQuery(Long id);

    ListarConfiguracionesQuery toListarQuery(Paginacion paginacion);

    ConfiguracionResponseDTO toResponse(ConfiguracionDTO dto);

    default Page<ConfiguracionResponseDTO> toResponsePage(Pagina<ConfiguracionDTO> pagina) {
        return new PageImpl<>(
                pagina.contenido().stream().map(this::toResponse).toList(),
                org.springframework.data.domain.PageRequest.of(pagina.pagina(), pagina.tamanio()),
                pagina.totalElementos()
        );
    }
}
