package com.alertabarrio.infrastructure.persistence.mapper;

import com.alertabarrio.domain.model.Barrio;
import com.alertabarrio.infrastructure.persistence.entity.BarrioEntity;
import com.alertabarrio.infrastructure.persistence.entity.CuadranteEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BarrioEntityMapper {

    default BarrioEntity toEntity(Barrio dominio) {
        if (dominio == null) return null;
        CuadranteEntity cuadrante = new CuadranteEntity();
        cuadrante.setId(dominio.getCuadranteId().value());
        BarrioEntity entity = new BarrioEntity(dominio.getNombre(), cuadrante);
        if (dominio.getId() != null) {
            entity.setId(dominio.getId().value());
        }
        return entity;
    }

    default Barrio toDomain(BarrioEntity entity) {
        if (entity == null) return null;
        return Barrio.reconstruir(
                entity.getId(),
                entity.getNombre(),
                entity.getCuadrante().getId()
        );
    }
}
