package com.alertabarrio.adapters.rest.mapper.mascotas;

import com.alertabarrio.adapters.rest.dto.mascotas.CiudadResponseDTO;
import com.alertabarrio.application.dto.mascotas.CiudadDTO;
import com.alertabarrio.domain.model.valueobject.Pagina;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CiudadDtoMapper {

    CiudadResponseDTO toResponse(CiudadDTO dto);

    default Pagina<CiudadResponseDTO> toResponsePage(Pagina<CiudadDTO> pagina) {
        return new Pagina<>(
                pagina.contenido().stream().map(this::toResponse).toList(),
                pagina.pagina(), pagina.tamanio(), pagina.totalElementos(), pagina.totalPaginas());
    }
}
