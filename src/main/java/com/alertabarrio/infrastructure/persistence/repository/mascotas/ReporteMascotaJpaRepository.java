package com.alertabarrio.infrastructure.persistence.repository.mascotas;

import com.alertabarrio.infrastructure.persistence.entity.mascotas.ReporteMascotaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReporteMascotaJpaRepository extends JpaRepository<ReporteMascotaEntity, Long> {

    /**
     * Feed público: filtra por estado y tipo de reporte, con ciudad opcional.
     * Todos los parámetros son obligatorios en la firma; los no deseados se
     * resuelven con null en el adapter (que decide el query a usar).
     */
    Page<ReporteMascotaEntity> findByEstadoAndTipoReporteAndCiudad_Id(
            String estado, String tipoReporte, Long ciudadId, Pageable pageable);

    Page<ReporteMascotaEntity> findByEstadoAndTipoReporte(
            String estado, String tipoReporte, Pageable pageable);

    Page<ReporteMascotaEntity> findByEstadoAndCiudad_Id(
            String estado, Long ciudadId, Pageable pageable);

    Page<ReporteMascotaEntity> findByEstado(
            String estado, Pageable pageable);

    /**
     * Feed público: excluye DELETED por defecto cuando no se filtra por estado.
     */
    Page<ReporteMascotaEntity> findByEstadoNot(
            String estado, Pageable pageable);

    Page<ReporteMascotaEntity> findByTipoReporte(
            String tipoReporte, Pageable pageable);

    Page<ReporteMascotaEntity> findByTipoReporteAndEstadoNot(
            String tipoReporte, String estado, Pageable pageable);

    Page<ReporteMascotaEntity> findByCiudad_Id(
            Long ciudadId, Pageable pageable);

    Page<ReporteMascotaEntity> findByCiudad_IdAndEstadoNot(
            Long ciudadId, String estado, Pageable pageable);

    Page<ReporteMascotaEntity> findByTipoReporteAndCiudad_Id(
            String tipoReporte, Long ciudadId, Pageable pageable);

    Page<ReporteMascotaEntity> findByTipoReporteAndCiudad_IdAndEstadoNot(
            String tipoReporte, Long ciudadId, String estado, Pageable pageable);

    Page<ReporteMascotaEntity> findByUsuario_Id(
            Long usuarioId, Pageable pageable);
}
