package com.alertabarrio.adapters.rest.mapper;

import com.alertabarrio.adapters.rest.dto.LocalidadResponseDTO;
import com.alertabarrio.application.dto.LocalidadDTO;
import com.alertabarrio.application.query.ListarLocalidadesQuery;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.model.valueobject.Paginacion;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LocalidadDtoMapper {

    LocalidadResponseDTO toResponse(LocalidadDTO dto);

    default ListarLocalidadesQuery toListarQuery(Long municipioId, Paginacion paginacion) {
        return new ListarLocalidadesQuery(municipioId, paginacion);
    }

    default Pagina<LocalidadResponseDTO> toResponsePage(Pagina<LocalidadDTO> pagina) {
        return new Pagina<>(
                pagina.contenido().stream().map(this::toResponse).toList(),
                pagina.pagina(), pagina.tamanio(), pagina.totalElementos(), pagina.totalPaginas());
    }
}
