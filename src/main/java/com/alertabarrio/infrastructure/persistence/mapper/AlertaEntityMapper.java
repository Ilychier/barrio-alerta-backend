package com.alertabarrio.infrastructure.persistence.mapper;

import com.alertabarrio.domain.model.Alerta;
import com.alertabarrio.infrastructure.persistence.entity.AlertaEntity;
import com.alertabarrio.infrastructure.persistence.entity.CategoriaEntity;
import com.alertabarrio.infrastructure.persistence.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AlertaEntityMapper {

    default AlertaEntity toEntity(Alerta dominio) {
        if (dominio == null) return null;
        UserEntity usuario = new UserEntity();
        usuario.setId(dominio.getUsuarioId().value());
        CategoriaEntity categoria = null;
        if (dominio.getCategoriaId() != null) {
            categoria = new CategoriaEntity();
            categoria.setId(dominio.getCategoriaId().value());
        }
        AlertaEntity entity = new AlertaEntity(
                dominio.getDescripcion(),
                dominio.isEsSos(),
                dominio.getFechaHora(),
                usuario,
                categoria
        );
        if (dominio.getId() != null) {
            entity.setId(dominio.getId().value());
        }
        return entity;
    }

    default Alerta toDomain(AlertaEntity entity) {
        if (entity == null) return null;
        return Alerta.reconstruir(
                entity.getId(),
                entity.getDescripcion(),
                entity.getEsSos(),
                entity.getFechaHora(),
                entity.getUsuario().getId(),
                entity.getCategoria() != null ? entity.getCategoria().getId() : null
        );
    }
}
