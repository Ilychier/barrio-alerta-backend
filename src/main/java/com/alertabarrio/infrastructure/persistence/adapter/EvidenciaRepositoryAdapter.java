package com.alertabarrio.infrastructure.persistence.adapter;

import com.alertabarrio.domain.model.Evidencia;
import com.alertabarrio.domain.model.valueobject.EvidenciaId;
import com.alertabarrio.domain.port.out.EvidenciaRepositoryPort;
import com.alertabarrio.infrastructure.persistence.entity.EvidenciaEntity;
import com.alertabarrio.infrastructure.persistence.mapper.EvidenciaEntityMapper;
import com.alertabarrio.infrastructure.persistence.repository.EvidenciaJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class EvidenciaRepositoryAdapter implements EvidenciaRepositoryPort {

    private final EvidenciaJpaRepository jpaRepository;
    private final EvidenciaEntityMapper mapper;

    public EvidenciaRepositoryAdapter(EvidenciaJpaRepository jpaRepository, EvidenciaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Evidencia save(Evidencia evidencia) {
        EvidenciaEntity entity = mapper.toEntity(evidencia);
        if (evidencia.getId() != null) {
            entity.setId(evidencia.getId().value());
        }
        EvidenciaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Evidencia> findById(EvidenciaId id) {
        return jpaRepository.findById(id.value())
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsById(EvidenciaId id) {
        return jpaRepository.existsById(id.value());
    }

    @Override
    public void deleteById(EvidenciaId id) {
        jpaRepository.deleteById(id.value());
    }

    @Override
    public Page<Evidencia> findByAlertaId(Long alertaId, Pageable pageable) {
        return jpaRepository.findByAlertaId(alertaId, pageable)
                .map(mapper::toDomain);
    }

    @Override
    public Page<Evidencia> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable)
                .map(mapper::toDomain);
    }
}
