package com.alertabarrio.infrastructure.persistence.mapper.mascotas;

import com.alertabarrio.domain.model.mascotas.Ciudad;
import com.alertabarrio.infrastructure.persistence.entity.mascotas.CiudadEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CiudadEntityMapper {

    default CiudadEntity toEntity(Ciudad dominio) {
        if (dominio == null) return null;
        CiudadEntity entity = new CiudadEntity(
                dominio.getNombre(),
                dominio.getDepartamento(),
                dominio.getPais()
        );
        if (dominio.getId() != null) {
            entity.setId(dominio.getId().value());
        }
        return entity;
    }

    default Ciudad toDomain(CiudadEntity entity) {
        if (entity == null) return null;
        return Ciudad.reconstruir(
                entity.getId(),
                entity.getNombre(),
                entity.getDepartamento(),
                entity.getPais()
        );
    }
}
