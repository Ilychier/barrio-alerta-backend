package com.alertabarrio.infrastructure.persistence.mapper.mascotas;

import com.alertabarrio.domain.model.mascotas.ReporteMascota;
import com.alertabarrio.infrastructure.persistence.entity.UserEntity;
import com.alertabarrio.infrastructure.persistence.entity.mascotas.CiudadEntity;
import com.alertabarrio.infrastructure.persistence.entity.mascotas.ReporteMascotaEntity;
import com.alertabarrio.infrastructure.persistence.entity.mascotas.TipoMascotaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReporteMascotaEntityMapper {

    default ReporteMascotaEntity toEntity(ReporteMascota dominio) {
        if (dominio == null) return null;
        UserEntity usuario = new UserEntity();
        usuario.setId(dominio.getUsuarioId().value());
        TipoMascotaEntity tipoMascota = new TipoMascotaEntity();
        tipoMascota.setId(dominio.getTipoMascotaId().value());
        CiudadEntity ciudad = new CiudadEntity();
        ciudad.setId(dominio.getCiudadId().value());
        ReporteMascotaEntity entity = new ReporteMascotaEntity(
                usuario,
                dominio.getTipoReporte().name(),
                tipoMascota,
                dominio.getOtroTipoMascota(),
                dominio.getFotoUrl(),
                ciudad,
                dominio.getUbicacion(),
                dominio.getTelefono().value(),
                dominio.getDescripcion(),
                dominio.getEstado().name(),
                dominio.getCreatedAt(),
                dominio.getUpdatedAt()
        );
        if (dominio.getId() != null) {
            entity.setId(dominio.getId().value());
        }
        return entity;
    }

    default ReporteMascota toDomain(ReporteMascotaEntity entity) {
        if (entity == null) return null;
        return ReporteMascota.reconstruir(
                entity.getId(),
                entity.getTipoReporte(),
                entity.getTipoMascota().getId(),
                entity.getCiudad().getId(),
                entity.getUbicacion(),
                entity.getTelefono(),
                entity.getDescripcion(),
                entity.getEstado(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getUsuario().getId(),
                entity.getOtroTipoMascota(),
                entity.getFotoUrl()
        );
    }
}
