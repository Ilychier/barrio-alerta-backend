package com.alertabarrio.application.usecase;

import com.alertabarrio.application.command.ActualizarCategoriaDescripcionCommand;
import com.alertabarrio.application.dto.CategoriaDescripcionDTO;
import com.alertabarrio.application.mapper.CategoriaDescripcionDomainMapper;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.model.CategoriaDescripcion;
import com.alertabarrio.domain.model.valueobject.CategoriaDescripcionId;
import com.alertabarrio.domain.port.in.ActualizarCategoriaDescripcionUseCase;
import com.alertabarrio.domain.port.out.CategoriaDescripcionRepositoryPort;

public class ActualizarCategoriaDescripcionUseCaseImpl implements ActualizarCategoriaDescripcionUseCase {

    private final CategoriaDescripcionRepositoryPort repository;
    private final CategoriaDescripcionDomainMapper mapper;

    public ActualizarCategoriaDescripcionUseCaseImpl(CategoriaDescripcionRepositoryPort repository, CategoriaDescripcionDomainMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public CategoriaDescripcionDTO execute(ActualizarCategoriaDescripcionCommand command) {
        CategoriaDescripcionId id = new CategoriaDescripcionId(command.id());
        repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CategoriaDescripcion", command.id()));

        CategoriaDescripcion actualizada = CategoriaDescripcion.reconstruir(command.id(), command.descripcion(), command.categoriaId(), command.imagenUrl());
        CategoriaDescripcion saved = repository.save(actualizada);
        return mapper.toDto(saved);
    }
}
