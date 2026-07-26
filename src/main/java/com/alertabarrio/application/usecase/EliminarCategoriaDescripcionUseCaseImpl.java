package com.alertabarrio.application.usecase;

import com.alertabarrio.application.command.EliminarCategoriaDescripcionCommand;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.model.valueobject.CategoriaDescripcionId;
import com.alertabarrio.domain.port.in.EliminarCategoriaDescripcionUseCase;
import com.alertabarrio.domain.port.out.CategoriaDescripcionRepositoryPort;

public class EliminarCategoriaDescripcionUseCaseImpl implements EliminarCategoriaDescripcionUseCase {

    private final CategoriaDescripcionRepositoryPort repository;

    public EliminarCategoriaDescripcionUseCaseImpl(CategoriaDescripcionRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public void execute(EliminarCategoriaDescripcionCommand command) {
        CategoriaDescripcionId id = new CategoriaDescripcionId(command.id());
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("CategoriaDescripcion", command.id());
        }
        repository.deleteById(id);
    }
}
