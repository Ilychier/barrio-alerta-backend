package com.alertabarrio.infrastructure.persistence.adapter.mascotas;

import com.alertabarrio.domain.model.mascotas.EstadoReporte;
import com.alertabarrio.domain.model.mascotas.ReporteMascota;
import com.alertabarrio.domain.model.mascotas.TipoReporte;
import com.alertabarrio.domain.model.mascotas.valueobject.ReporteMascotaId;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.model.valueobject.Paginacion;
import com.alertabarrio.domain.model.valueobject.UsuarioId;
import com.alertabarrio.domain.port.out.mascotas.ReporteMascotaRepositoryPort;
import com.alertabarrio.infrastructure.persistence.entity.mascotas.ReporteMascotaEntity;
import com.alertabarrio.infrastructure.persistence.mapper.mascotas.ReporteMascotaEntityMapper;
import com.alertabarrio.infrastructure.persistence.repository.mascotas.ReporteMascotaJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ReporteMascotaRepositoryAdapter implements ReporteMascotaRepositoryPort {

    private final ReporteMascotaJpaRepository jpaRepository;
    private final ReporteMascotaEntityMapper mapper;

    public ReporteMascotaRepositoryAdapter(ReporteMascotaJpaRepository jpaRepository,
                                           ReporteMascotaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public ReporteMascota save(ReporteMascota reporte) {
        ReporteMascotaEntity entity = mapper.toEntity(reporte);
        if (reporte.getId() != null) {
            entity.setId(reporte.getId().value());
        }
        ReporteMascotaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<ReporteMascota> findById(ReporteMascotaId id) {
        return jpaRepository.findById(id.value())
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsById(ReporteMascotaId id) {
        return jpaRepository.existsById(id.value());
    }

    @Override
    public void deleteById(ReporteMascotaId id) {
        jpaRepository.deleteById(id.value());
    }

    @Override
    public Pagina<ReporteMascota> findByFilters(EstadoReporte estado, TipoReporte tipoReporte,
                                                Long ciudadId, Paginacion paginacion) {
        Pageable pageable = MascotaPaginacionHelper.toPageable(paginacion);
        String estadoStr = estado != null ? estado.name() : null;
        String tipoStr = tipoReporte != null ? tipoReporte.name() : null;
        Page<ReporteMascotaEntity> page;

        if (estadoStr != null && tipoStr != null && ciudadId != null) {
            page = jpaRepository.findByEstadoAndTipoReporteAndCiudad_Id(estadoStr, tipoStr, ciudadId, pageable);
        } else if (estadoStr != null && tipoStr != null) {
            page = jpaRepository.findByEstadoAndTipoReporte(estadoStr, tipoStr, pageable);
        } else if (estadoStr != null && ciudadId != null) {
            page = jpaRepository.findByEstadoAndCiudad_Id(estadoStr, ciudadId, pageable);
        } else if (estadoStr != null) {
            page = jpaRepository.findByEstado(estadoStr, pageable);
        } else {
            page = jpaRepository.findAll(pageable);
        }

        return new Pagina<>(
                page.getContent().stream().map(mapper::toDomain).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    @Override
    public Pagina<ReporteMascota> findByEstado(EstadoReporte estado, Paginacion paginacion) {
        Pageable pageable = MascotaPaginacionHelper.toPageable(paginacion);
        Page<ReporteMascotaEntity> page = jpaRepository.findByEstado(estado.name(), pageable);
        return new Pagina<>(
                page.getContent().stream().map(mapper::toDomain).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    @Override
    public Pagina<ReporteMascota> findByUsuarioId(UsuarioId usuarioId, Paginacion paginacion) {
        Pageable pageable = MascotaPaginacionHelper.toPageable(paginacion);
        Page<ReporteMascotaEntity> page = jpaRepository.findByUsuario_Id(usuarioId.value(), pageable);
        return new Pagina<>(
                page.getContent().stream().map(mapper::toDomain).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
