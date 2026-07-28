package com.alertabarrio.application.mapper;

import com.alertabarrio.application.dto.UsuarioDTO;
import com.alertabarrio.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioDomainMapper {

    @Mapping(target = "id", source = "id.value")
    @Mapping(target = "email", source = "email.value")
    @Mapping(target = "phone", source = "phone.value")
    @Mapping(target = "barrioId", source = "barrioId.value")
    UsuarioDTO toDto(User user);
}
