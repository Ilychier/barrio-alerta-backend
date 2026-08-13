package com.alertabarrio.infrastructure.persistence.repository.mascotas;

import com.alertabarrio.infrastructure.persistence.entity.mascotas.CiudadEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CiudadJpaRepository extends JpaRepository<CiudadEntity, Long> {
}
