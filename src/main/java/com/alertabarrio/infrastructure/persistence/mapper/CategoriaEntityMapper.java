package com.alertabarrio.infrastructure.persistence.mapper;

import com.alertabarrio.domain.model.Categoria;
import com.alertabarrio.infrastructure.persistence.entity.CategoriaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoriaEntityMapper {

    default CategoriaEntity toEntity(Categoria dominio) {
        if (dominio == null) return null;
        CategoriaEntity entity = new CategoriaEntity();
        entity.setNombre(dominio.getNombre());
        entity.setIconoReferencia(dominio.getIconoReferencia());
        if (dominio.getId() != null) {
            entity.setId(dominio.getId().value());
        }
        return entity;
    }

    default Categoria toDomain(CategoriaEntity entity) {
        if (entity == null) return null;
        return Categoria.reconstruir(entity.getId(), entity.getNombre(), entity.getIconoReferencia());
    }
}
