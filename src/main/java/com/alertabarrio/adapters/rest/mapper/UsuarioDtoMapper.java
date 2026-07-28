package com.alertabarrio.adapters.rest.mapper;

import com.alertabarrio.adapters.rest.dto.UsuarioRequestDTO;
import com.alertabarrio.adapters.rest.dto.UsuarioResponseDTO;
import com.alertabarrio.application.command.*;
import com.alertabarrio.application.dto.UsuarioDTO;
import com.alertabarrio.application.query.BuscarUsuarioQuery;
import com.alertabarrio.application.query.ListarUsuariosQuery;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.model.valueobject.Paginacion;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

@Mapper(componentModel = "spring")
public interface UsuarioDtoMapper {

    RegistrarUsuarioCommand toCrearCommand(UsuarioRequestDTO dto);

    ActualizarUsuarioCommand toActualizarCommand(Long id, UsuarioRequestDTO dto);

    ParchearUsuarioCommand toParchearCommand(Long id, UsuarioRequestDTO dto);

    EliminarUsuarioCommand toEliminarCommand(Long id);

    BuscarUsuarioQuery toBuscarQuery(Long id);

    ListarUsuariosQuery toListarQuery(Paginacion paginacion);

    UsuarioResponseDTO toResponse(UsuarioDTO dto);

    default Page<UsuarioResponseDTO> toResponsePage(Pagina<UsuarioDTO> pagina) {
        return new PageImpl<>(
                pagina.contenido().stream().map(this::toResponse).toList(),
                org.springframework.data.domain.PageRequest.of(pagina.pagina(), pagina.tamanio()),
                pagina.totalElementos()
        );
    }
}
