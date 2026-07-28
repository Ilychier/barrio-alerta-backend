package com.alertabarrio.application.usecase;

import com.alertabarrio.application.UseCase;
import com.alertabarrio.application.command.EliminarCategoriaCommand;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.model.valueobject.CategoriaId;
import com.alertabarrio.domain.port.in.EliminarCategoriaUseCase;
import com.alertabarrio.domain.port.out.CategoriaRepositoryPort;

@UseCase
public class EliminarCategoriaUseCaseImpl implements EliminarCategoriaUseCase {

    private final CategoriaRepositoryPort categoriaRepository;

    public EliminarCategoriaUseCaseImpl(CategoriaRepositoryPort categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public void execute(EliminarCategoriaCommand command) {
        CategoriaId id = new CategoriaId(command.id());
        if (!categoriaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Categoria", command.id());
        }
        categoriaRepository.deleteById(id);
    }
}
