package com.alertabarrio.infrastructure.persistence.adapter;

import com.alertabarrio.domain.model.Evidencia;
import com.alertabarrio.domain.model.valueobject.EvidenciaId;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.model.valueobject.Paginacion;
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
    public Pagina<Evidencia> findByAlertaId(Long alertaId, Paginacion paginacion) {
        Pageable pageable = PaginacionHelper.toPageable(paginacion);
        Page<EvidenciaEntity> page = jpaRepository.findByAlertaId(alertaId, pageable);
        return new Pagina<>(
                page.getContent().stream().map(mapper::toDomain).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    @Override
    public Pagina<Evidencia> findAll(Paginacion paginacion) {
        Pageable pageable = PaginacionHelper.toPageable(paginacion);
        Page<EvidenciaEntity> page = jpaRepository.findAll(pageable);
        return new Pagina<>(
                page.getContent().stream().map(mapper::toDomain).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
