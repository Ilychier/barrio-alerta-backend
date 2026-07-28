package com.alertabarrio.infrastructure.persistence.adapter;

import com.alertabarrio.domain.model.Configuracion;
import com.alertabarrio.domain.model.valueobject.ConfiguracionId;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.model.valueobject.Paginacion;
import com.alertabarrio.domain.port.out.ConfiguracionRepositoryPort;
import com.alertabarrio.infrastructure.persistence.entity.ConfiguracionEntity;
import com.alertabarrio.infrastructure.persistence.mapper.ConfiguracionEntityMapper;
import com.alertabarrio.infrastructure.persistence.repository.ConfiguracionJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ConfiguracionRepositoryAdapter implements ConfiguracionRepositoryPort {

    private final ConfiguracionJpaRepository jpaRepository;
    private final ConfiguracionEntityMapper mapper;

    public ConfiguracionRepositoryAdapter(ConfiguracionJpaRepository jpaRepository, ConfiguracionEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Configuracion save(Configuracion config) {
        ConfiguracionEntity entity = mapper.toEntity(config);
        if (config.getId() != null) {
            entity.setId(config.getId().value());
        }
        ConfiguracionEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Configuracion> findById(ConfiguracionId id) {
        return jpaRepository.findById(id.value())
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsById(ConfiguracionId id) {
        return jpaRepository.existsById(id.value());
    }

    @Override
    public void deleteById(ConfiguracionId id) {
        jpaRepository.deleteById(id.value());
    }

    @Override
    public Optional<Configuracion> findByUsuarioId(Long usuarioId) {
        return jpaRepository.findByUsuarioId(usuarioId)
                .map(mapper::toDomain);
    }

    @Override
    public Pagina<Configuracion> findAll(Paginacion paginacion) {
        Pageable pageable = PaginacionHelper.toPageable(paginacion);
        Page<ConfiguracionEntity> page = jpaRepository.findAll(pageable);
        return new Pagina<>(
                page.getContent().stream().map(mapper::toDomain).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
