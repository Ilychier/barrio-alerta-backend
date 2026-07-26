package com.alertabarrio.infrastructure.persistence.adapter;

import com.alertabarrio.domain.model.Barrio;
import com.alertabarrio.domain.model.valueobject.BarrioId;
import com.alertabarrio.domain.port.out.BarrioRepositoryPort;
import com.alertabarrio.infrastructure.persistence.entity.BarrioEntity;
import com.alertabarrio.infrastructure.persistence.mapper.BarrioEntityMapper;
import com.alertabarrio.infrastructure.persistence.repository.BarrioJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class BarrioRepositoryAdapter implements BarrioRepositoryPort {

    private final BarrioJpaRepository jpaRepository;
    private final BarrioEntityMapper mapper;

    public BarrioRepositoryAdapter(BarrioJpaRepository jpaRepository, BarrioEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Barrio save(Barrio barrio) {
        BarrioEntity entity = mapper.toEntity(barrio);
        if (barrio.getId() != null) {
            entity.setId(barrio.getId().value());
        }
        BarrioEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Barrio> findById(BarrioId id) {
        return jpaRepository.findById(id.value())
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsById(BarrioId id) {
        return jpaRepository.existsById(id.value());
    }

    @Override
    public void deleteById(BarrioId id) {
        jpaRepository.deleteById(id.value());
    }

    @Override
    public boolean existsByNombre(String nombre) {
        return jpaRepository.existsByNombre(nombre);
    }

    @Override
    public Page<Barrio> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable)
                .map(mapper::toDomain);
    }
}
