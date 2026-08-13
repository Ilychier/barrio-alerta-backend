package com.alertabarrio.infrastructure.persistence.repository.mascotas;

import com.alertabarrio.infrastructure.persistence.entity.mascotas.ReporteMascotaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Repositorio Spring Data de {@link ReporteMascotaEntity}.
 * <p>
 * El feed público usa {@link JpaSpecificationExecutor} para construir filtros
 * dinámicos (estado, tipo, ciudad, búsqueda) sin multiplicar métodos derivados.
 * Los métodos derivados restantes cubren consultas fijas (historias, mis reportes).
 */
public interface ReporteMascotaJpaRepository
        extends JpaRepository<ReporteMascotaEntity, Long>, JpaSpecificationExecutor<ReporteMascotaEntity> {

    /**
     * Historias de rescate: reportes con un estado específico.
     */
    Page<ReporteMascotaEntity> findByEstado(String estado, Pageable pageable);

    /**
     * Mis reportes: reportes de un usuario (incluye DELETED).
     */
    Page<ReporteMascotaEntity> findByUsuario_Id(Long usuarioId, Pageable pageable);
}
