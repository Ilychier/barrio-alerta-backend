package com.alertabarrio.infrastructure.persistence.mapper;

import com.alertabarrio.domain.model.Cuadrante;
import com.alertabarrio.infrastructure.persistence.entity.CuadranteEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CuadranteEntityMapper {

    default CuadranteEntity toEntity(Cuadrante dominio) {
        if (dominio == null) return null;
        CuadranteEntity entity = new CuadranteEntity();
        entity.setNombreUnidad(dominio.getNombreUnidad());
        entity.setTelefonoEmergencia(dominio.getTelefonoEmergencia());
        if (dominio.getId() != null) {
            entity.setId(dominio.getId().value());
        }
        return entity;
    }

    default Cuadrante toDomain(CuadranteEntity entity) {
        if (entity == null) return null;
        return Cuadrante.reconstruir(entity.getId(), entity.getNombreUnidad(), entity.getTelefonoEmergencia());
    }
}
