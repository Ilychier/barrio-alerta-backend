package com.alertabarrio.infrastructure.persistence.adapter;

import com.alertabarrio.domain.model.CiudadInfo;
import com.alertabarrio.domain.model.valueobject.CiudadId;
import com.alertabarrio.domain.port.out.CiudadInfoPort;
import com.alertabarrio.infrastructure.persistence.entity.CiudadInfoEntity;
import com.alertabarrio.infrastructure.persistence.repository.CiudadInfoJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CiudadInfoAdapter implements CiudadInfoPort {

    private final CiudadInfoJpaRepository jpaRepository;

    public CiudadInfoAdapter(CiudadInfoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<CiudadInfo> findById(CiudadId id) {
        return jpaRepository.findById(id.value())
                .map(this::toInfo);
    }

    private CiudadInfo toInfo(CiudadInfoEntity entity) {
        return new CiudadInfo(
                entity.getId(),
                entity.getNombre(),
                entity.getDepartamento(),
                entity.getPais()
        );
    }
}
