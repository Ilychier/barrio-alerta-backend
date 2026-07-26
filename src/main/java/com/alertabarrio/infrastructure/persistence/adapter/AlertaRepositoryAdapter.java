package com.alertabarrio.infrastructure.persistence.adapter;

import com.alertabarrio.domain.model.Alerta;
import com.alertabarrio.domain.model.valueobject.AlertaId;
import com.alertabarrio.domain.port.out.AlertaRepositoryPort;
import com.alertabarrio.infrastructure.persistence.entity.AlertaEntity;
import com.alertabarrio.infrastructure.persistence.mapper.AlertaEntityMapper;
import com.alertabarrio.infrastructure.persistence.repository.AlertaJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public class AlertaRepositoryAdapter implements AlertaRepositoryPort {

    private final AlertaJpaRepository jpaRepository;
    private final AlertaEntityMapper mapper;

    public AlertaRepositoryAdapter(AlertaJpaRepository jpaRepository, AlertaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Alerta save(Alerta alerta) {
        AlertaEntity entity = mapper.toEntity(alerta);
        if (alerta.getId() != null) {
            entity.setId(alerta.getId().value());
        }
        AlertaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Alerta> findById(AlertaId id) {
        return jpaRepository.findById(id.value())
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsById(AlertaId id) {
        return jpaRepository.existsById(id.value());
    }

    @Override
    public void deleteById(AlertaId id) {
        jpaRepository.deleteById(id.value());
    }

    @Override
    public Page<Alerta> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable)
                .map(mapper::toDomain);
    }

    @Override
    public Page<Alerta> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin, Pageable pageable) {
        return jpaRepository.findByFechaHoraBetween(inicio, fin, pageable)
                .map(mapper::toDomain);
    }

    @Override
    public Page<Alerta> findByUsuario_Barrio_IdAndFechaHoraBetween(Long barrioId, LocalDateTime inicio, LocalDateTime fin, Pageable pageable) {
        return jpaRepository.findByUsuario_Barrio_IdAndFechaHoraBetween(barrioId, inicio, fin, pageable)
                .map(mapper::toDomain);
    }
}
