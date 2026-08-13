package com.alertabarrio.infrastructure.persistence.mapper.mascotas;

import com.alertabarrio.domain.model.mascotas.TipoMascota;
import com.alertabarrio.infrastructure.persistence.entity.mascotas.TipoMascotaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TipoMascotaEntityMapper {

    default TipoMascotaEntity toEntity(TipoMascota dominio) {
        if (dominio == null) return null;
        TipoMascotaEntity entity = new TipoMascotaEntity(
                dominio.getNombre(),
                dominio.isActivo()
        );
        if (dominio.getId() != null) {
            entity.setId(dominio.getId().value());
        }
        return entity;
    }

    default TipoMascota toDomain(TipoMascotaEntity entity) {
        if (entity == null) return null;
        return TipoMascota.reconstruir(
                entity.getId(),
                entity.getNombre(),
                entity.getActivo()
        );
    }
}
