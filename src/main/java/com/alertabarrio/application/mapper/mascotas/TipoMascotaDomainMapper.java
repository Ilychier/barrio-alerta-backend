package com.alertabarrio.application.mapper.mascotas;

import com.alertabarrio.application.dto.mascotas.TipoMascotaDTO;
import com.alertabarrio.domain.model.mascotas.TipoMascota;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TipoMascotaDomainMapper {

    @Mapping(target = "id", source = "id.value")
    TipoMascotaDTO toDto(TipoMascota tipoMascota);
}
