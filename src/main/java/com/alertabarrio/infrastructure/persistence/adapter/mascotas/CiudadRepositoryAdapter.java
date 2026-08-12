package com.alertabarrio.infrastructure.persistence.adapter.mascotas;

import com.alertabarrio.domain.model.mascotas.Ciudad;
import com.alertabarrio.domain.model.mascotas.valueobject.CiudadId;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.model.valueobject.Paginacion;
import com.alertabarrio.domain.port.out.mascotas.CiudadRepositoryPort;
import com.alertabarrio.infrastructure.persistence.entity.mascotas.CiudadEntity;
import com.alertabarrio.infrastructure.persistence.mapper.mascotas.CiudadEntityMapper;
import com.alertabarrio.infrastructure.persistence.repository.mascotas.CiudadJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CiudadRepositoryAdapter implements CiudadRepositoryPort {

    private final CiudadJpaRepository jpaRepository;
    private final CiudadEntityMapper mapper;

    public CiudadRepositoryAdapter(CiudadJpaRepository jpaRepository, CiudadEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Ciudad save(Ciudad ciudad) {
        CiudadEntity entity = mapper.toEntity(ciudad);
        if (ciudad.getId() != null) {
            entity.setId(ciudad.getId().value());
        }
        CiudadEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Ciudad> findById(CiudadId id) {
        return jpaRepository.findById(id.value())
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsById(CiudadId id) {
        return jpaRepository.existsById(id.value());
    }

    @Override
    public Pagina<Ciudad> findAll(Paginacion paginacion) {
        Pageable pageable = MascotaPaginacionHelper.toPageable(paginacion);
        Page<CiudadEntity> page = jpaRepository.findAll(pageable);
        return new Pagina<>(
                page.getContent().stream().map(mapper::toDomain).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
