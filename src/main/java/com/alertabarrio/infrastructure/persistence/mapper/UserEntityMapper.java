package com.alertabarrio.infrastructure.persistence.mapper;

import com.alertabarrio.domain.model.User;
import com.alertabarrio.infrastructure.persistence.entity.BarrioEntity;
import com.alertabarrio.infrastructure.persistence.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserEntityMapper {

    default UserEntity toEntity(User dominio) {
        if (dominio == null) return null;
        BarrioEntity barrio = new BarrioEntity();
        barrio.setId(dominio.getBarrioId().value());
        UserEntity entity = new UserEntity(
                dominio.getName(),
                dominio.getEmail().value(),
                dominio.getPhone().value(),
                dominio.getAddress(),
                dominio.getPassword(),
                barrio
        );
        if (dominio.getId() != null) {
            entity.setId(dominio.getId().value());
        }
        return entity;
    }

    default User toDomain(UserEntity entity) {
        if (entity == null) return null;
        return User.reconstruir(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getAddress(),
                entity.getPassword(),
                entity.getBarrio().getId()
        );
    }
}
