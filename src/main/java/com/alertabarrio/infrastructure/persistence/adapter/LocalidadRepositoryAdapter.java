package com.alertabarrio.infrastructure.persistence.adapter;

import com.alertabarrio.domain.model.Localidad;
import com.alertabarrio.domain.model.valueobject.LocalidadId;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.model.valueobject.Paginacion;
import com.alertabarrio.domain.port.out.LocalidadRepositoryPort;
import com.alertabarrio.infrastructure.persistence.entity.LocalidadEntity;
import com.alertabarrio.infrastructure.persistence.mapper.LocalidadEntityMapper;
import com.alertabarrio.infrastructure.persistence.repository.LocalidadJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class LocalidadRepositoryAdapter implements LocalidadRepositoryPort {

    private final LocalidadJpaRepository jpaRepository;
    private final LocalidadEntityMapper mapper;

    public LocalidadRepositoryAdapter(LocalidadJpaRepository jpaRepository, LocalidadEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Localidad> findById(LocalidadId id) {
        return jpaRepository.findById(id.value())
                .map(mapper::toDomain);
    }

    @Override
    public Pagina<Localidad> findByMunicipioId(Long municipioId, Paginacion paginacion) {
        Pageable pageable = PaginacionHelper.toPageable(paginacion);
        Page<LocalidadEntity> page = jpaRepository.findByMunicipio_Id(municipioId, pageable);
        return new Pagina<>(
                page.getContent().stream().map(mapper::toDomain).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
