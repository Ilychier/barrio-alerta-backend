package com.alertabarrio.infrastructure.persistence.repository.mascotas;

import com.alertabarrio.infrastructure.persistence.entity.mascotas.TipoMascotaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TipoMascotaJpaRepository extends JpaRepository<TipoMascotaEntity, Long> {

    List<TipoMascotaEntity> findByActivoTrue();
}
