package com.alertabarrio.infrastructure.persistence.repository;

import com.alertabarrio.infrastructure.persistence.entity.CiudadInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CiudadInfoJpaRepository extends JpaRepository<CiudadInfoEntity, Long> {
}
