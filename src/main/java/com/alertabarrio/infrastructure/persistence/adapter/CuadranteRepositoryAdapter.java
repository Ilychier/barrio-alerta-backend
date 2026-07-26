package com.alertabarrio.infrastructure.persistence.adapter;

import com.alertabarrio.domain.model.Cuadrante;
import com.alertabarrio.domain.model.valueobject.CuadranteId;
import com.alertabarrio.domain.port.out.CuadranteRepositoryPort;
import com.alertabarrio.infrastructure.persistence.entity.CuadranteEntity;
import com.alertabarrio.infrastructure.persistence.mapper.CuadranteEntityMapper;
import com.alertabarrio.infrastructure.persistence.repository.CuadranteJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CuadranteRepositoryAdapter implements CuadranteRepositoryPort {

    private final CuadranteJpaRepository jpaRepository;
    private final CuadranteEntityMapper mapper;

    public CuadranteRepositoryAdapter(CuadranteJpaRepository jpaRepository, CuadranteEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Cuadrante save(Cuadrante cuadrante) {
        CuadranteEntity entity = mapper.toEntity(cuadrante);
        if (cuadrante.getId() != null) {
            entity.setId(cuadrante.getId().value());
        }
        CuadranteEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Cuadrante> findById(CuadranteId id) {
        return jpaRepository.findById(id.value())
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsById(CuadranteId id) {
        return jpaRepository.existsById(id.value());
    }

    @Override
    public void deleteById(CuadranteId id) {
        jpaRepository.deleteById(id.value());
    }

    @Override
    public boolean existsByTelefonoEmergencia(String telefonoEmergencia) {
        return jpaRepository.existsByTelefonoEmergencia(telefonoEmergencia);
    }

    @Override
    public Page<Cuadrante> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable)
                .map(mapper::toDomain);
    }
}
