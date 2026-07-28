package com.alertabarrio.infrastructure.persistence.mapper;

import com.alertabarrio.domain.model.Configuracion;
import com.alertabarrio.infrastructure.persistence.entity.ConfiguracionEntity;
import com.alertabarrio.infrastructure.persistence.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConfiguracionEntityMapper {

    default ConfiguracionEntity toEntity(Configuracion dominio) {
        if (dominio == null) return null;
        UserEntity usuario = new UserEntity();
        usuario.setId(dominio.getUsuarioId().value());
        ConfiguracionEntity entity = new ConfiguracionEntity(
                usuario, dominio.isRecibirNotificaciones(), dominio.isModoSilencioso());
        if (dominio.getId() != null) {
            entity.setId(dominio.getId().value());
        }
        return entity;
    }

    default Configuracion toDomain(ConfiguracionEntity entity) {
        if (entity == null) return null;
        return Configuracion.reconstruir(
                entity.getId(),
                entity.getUsuario().getId(),
                entity.getRecibirNotificaciones(),
                entity.getModoSilencioso()
        );
    }
}
