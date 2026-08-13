package com.alertabarrio.infrastructure.persistence.mapper;

import com.alertabarrio.domain.model.Localidad;
import com.alertabarrio.infrastructure.persistence.entity.CiudadInfoEntity;
import com.alertabarrio.infrastructure.persistence.entity.LocalidadEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LocalidadEntityMapper {

    default LocalidadEntity toEntity(Localidad dominio) {
        if (dominio == null) return null;
        CiudadInfoEntity municipio = new CiudadInfoEntity();
        municipio.setId(dominio.getMunicipioId());
        LocalidadEntity entity = new LocalidadEntity(dominio.getNombre(), municipio);
        if (dominio.getId() != null) {
            entity.setId(dominio.getId().value());
        }
        return entity;
    }

    default Localidad toDomain(LocalidadEntity entity) {
        if (entity == null) return null;
        return Localidad.reconstruir(
                entity.getId(),
                entity.getNombre(),
                entity.getMunicipio().getId()
        );
    }
}
