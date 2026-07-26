package com.alertabarrio.infrastructure.persistence.adapter;

import com.alertabarrio.domain.model.CategoriaDescripcion;
import com.alertabarrio.domain.model.valueobject.CategoriaDescripcionId;
import com.alertabarrio.domain.port.out.CategoriaDescripcionRepositoryPort;
import com.alertabarrio.infrastructure.persistence.entity.CategoriaDescripcionEntity;
import com.alertabarrio.infrastructure.persistence.mapper.CategoriaDescripcionEntityMapper;
import com.alertabarrio.infrastructure.persistence.repository.CategoriaDescripcionJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CategoriaDescripcionRepositoryAdapter implements CategoriaDescripcionRepositoryPort {

    private final CategoriaDescripcionJpaRepository jpaRepository;
    private final CategoriaDescripcionEntityMapper mapper;

    public CategoriaDescripcionRepositoryAdapter(CategoriaDescripcionJpaRepository jpaRepository, CategoriaDescripcionEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public CategoriaDescripcion save(CategoriaDescripcion cd) {
        CategoriaDescripcionEntity entity = mapper.toEntity(cd);
        if (cd.getId() != null) {
            entity.setId(cd.getId().value());
        }
        CategoriaDescripcionEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<CategoriaDescripcion> findById(CategoriaDescripcionId id) {
        return jpaRepository.findById(id.value())
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsById(CategoriaDescripcionId id) {
        return jpaRepository.existsById(id.value());
    }

    @Override
    public void deleteById(CategoriaDescripcionId id) {
        jpaRepository.deleteById(id.value());
    }

    @Override
    public Page<CategoriaDescripcion> findByCategoriaId(Long categoriaId, Pageable pageable) {
        return jpaRepository.findByCategoriaId(categoriaId, pageable)
                .map(mapper::toDomain);
    }

    @Override
    public Page<CategoriaDescripcion> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable)
                .map(mapper::toDomain);
    }
}
