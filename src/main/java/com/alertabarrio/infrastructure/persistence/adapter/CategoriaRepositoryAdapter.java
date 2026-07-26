package com.alertabarrio.infrastructure.persistence.adapter;

import com.alertabarrio.domain.model.Categoria;
import com.alertabarrio.domain.model.valueobject.CategoriaId;
import com.alertabarrio.domain.port.out.CategoriaRepositoryPort;
import com.alertabarrio.infrastructure.persistence.entity.CategoriaEntity;
import com.alertabarrio.infrastructure.persistence.mapper.CategoriaEntityMapper;
import com.alertabarrio.infrastructure.persistence.repository.CategoriaJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CategoriaRepositoryAdapter implements CategoriaRepositoryPort {

    private final CategoriaJpaRepository jpaRepository;
    private final CategoriaEntityMapper mapper;

    public CategoriaRepositoryAdapter(CategoriaJpaRepository jpaRepository, CategoriaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Categoria save(Categoria categoria) {
        CategoriaEntity entity = mapper.toEntity(categoria);
        if (categoria.getId() != null) {
            entity.setId(categoria.getId().value());
        }
        CategoriaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Categoria> findById(CategoriaId id) {
        return jpaRepository.findById(id.value())
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsById(CategoriaId id) {
        return jpaRepository.existsById(id.value());
    }

    @Override
    public void deleteById(CategoriaId id) {
        jpaRepository.deleteById(id.value());
    }

    @Override
    public boolean existsByNombre(String nombre) {
        return jpaRepository.existsByNombre(nombre);
    }

    @Override
    public Page<Categoria> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable)
                .map(mapper::toDomain);
    }
}
