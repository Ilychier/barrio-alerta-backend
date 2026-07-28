package com.alertabarrio.infrastructure.persistence.mapper;

import com.alertabarrio.domain.model.CategoriaDescripcion;
import com.alertabarrio.infrastructure.persistence.entity.CategoriaDescripcionEntity;
import com.alertabarrio.infrastructure.persistence.entity.CategoriaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoriaDescripcionEntityMapper {

    default CategoriaDescripcionEntity toEntity(CategoriaDescripcion dominio) {
        if (dominio == null) return null;
        CategoriaEntity categoria = new CategoriaEntity();
        categoria.setId(dominio.getCategoriaId().value());
        CategoriaDescripcionEntity entity = new CategoriaDescripcionEntity(
                dominio.getDescripcion(), categoria, dominio.getImagenUrl());
        if (dominio.getId() != null) {
            entity.setId(dominio.getId().value());
        }
        return entity;
    }

    default CategoriaDescripcion toDomain(CategoriaDescripcionEntity entity) {
        if (entity == null) return null;
        return CategoriaDescripcion.reconstruir(
                entity.getId(),
                entity.getDescripcion(),
                entity.getCategoria().getId(),
                entity.getImagenUrl()
        );
    }
}
