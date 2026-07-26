package com.alertabarrio.application.usecase;

import com.alertabarrio.application.UseCase;
import com.alertabarrio.application.command.CrearCategoriaDescripcionCommand;
import com.alertabarrio.application.dto.CategoriaDescripcionDTO;
import com.alertabarrio.application.mapper.CategoriaDescripcionDomainMapper;
import com.alertabarrio.domain.model.CategoriaDescripcion;
import com.alertabarrio.domain.port.in.CrearCategoriaDescripcionUseCase;
import com.alertabarrio.domain.port.out.CategoriaDescripcionRepositoryPort;

@UseCase
public class CrearCategoriaDescripcionUseCaseImpl implements CrearCategoriaDescripcionUseCase {

    private final CategoriaDescripcionRepositoryPort repository;
    private final CategoriaDescripcionDomainMapper mapper;

    public CrearCategoriaDescripcionUseCaseImpl(CategoriaDescripcionRepositoryPort repository, CategoriaDescripcionDomainMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public CategoriaDescripcionDTO execute(CrearCategoriaDescripcionCommand command) {
        CategoriaDescripcion cd = CategoriaDescripcion.crear(command.descripcion(), command.categoriaId(), command.imagenUrl());
        CategoriaDescripcion saved = repository.save(cd);
        return mapper.toDto(saved);
    }
}
