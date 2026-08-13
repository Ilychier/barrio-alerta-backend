package com.alertabarrio.infrastructure.persistence.adapter.mascotas;

import com.alertabarrio.domain.model.mascotas.TipoMascota;
import com.alertabarrio.domain.model.mascotas.valueobject.TipoMascotaId;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.model.valueobject.Paginacion;
import com.alertabarrio.domain.port.out.mascotas.TipoMascotaRepositoryPort;
import com.alertabarrio.infrastructure.persistence.entity.mascotas.TipoMascotaEntity;
import com.alertabarrio.infrastructure.persistence.mapper.mascotas.TipoMascotaEntityMapper;
import com.alertabarrio.infrastructure.persistence.repository.mascotas.TipoMascotaJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TipoMascotaRepositoryAdapter implements TipoMascotaRepositoryPort {

    private final TipoMascotaJpaRepository jpaRepository;
    private final TipoMascotaEntityMapper mapper;

    public TipoMascotaRepositoryAdapter(TipoMascotaJpaRepository jpaRepository,
                                        TipoMascotaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public TipoMascota save(TipoMascota tipoMascota) {
        TipoMascotaEntity entity = mapper.toEntity(tipoMascota);
        if (tipoMascota.getId() != null) {
            entity.setId(tipoMascota.getId().value());
        }
        TipoMascotaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<TipoMascota> findById(TipoMascotaId id) {
        return jpaRepository.findById(id.value())
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsById(TipoMascotaId id) {
        return jpaRepository.existsById(id.value());
    }

    @Override
    public List<TipoMascota> findAllActivos() {
        return jpaRepository.findByActivoTrue().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Pagina<TipoMascota> findAll(Paginacion paginacion) {
        Pageable pageable = MascotaPaginacionHelper.toPageable(paginacion);
        Page<TipoMascotaEntity> page = jpaRepository.findAll(pageable);
        return new Pagina<>(
                page.getContent().stream().map(mapper::toDomain).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
