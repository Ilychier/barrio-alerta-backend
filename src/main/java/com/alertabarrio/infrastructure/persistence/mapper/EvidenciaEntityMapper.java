package com.alertabarrio.infrastructure.persistence.mapper;

import com.alertabarrio.domain.model.Evidencia;
import com.alertabarrio.infrastructure.persistence.entity.AlertaEntity;
import com.alertabarrio.infrastructure.persistence.entity.EvidenciaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EvidenciaEntityMapper {

    default EvidenciaEntity toEntity(Evidencia dominio) {
        if (dominio == null) return null;
        AlertaEntity alerta = new AlertaEntity();
        alerta.setId(dominio.getAlertaId().value());
        EvidenciaEntity entity = new EvidenciaEntity(
                dominio.getArchivoUrl(),
                dominio.getFechaSubida(),
                alerta
        );
        if (dominio.getId() != null) {
            entity.setId(dominio.getId().value());
        }
        return entity;
    }

    default Evidencia toDomain(EvidenciaEntity entity) {
        if (entity == null) return null;
        return Evidencia.reconstruir(
                entity.getId(),
                entity.getArchivoUrl(),
                entity.getFechaSubida(),
                entity.getAlerta().getId()
        );
    }
}
