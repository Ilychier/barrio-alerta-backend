package com.alertabarrio.application.usecase;

import com.alertabarrio.application.UseCase;
import com.alertabarrio.application.command.ActualizarCategoriaCommand;
import com.alertabarrio.application.dto.CategoriaDTO;
import com.alertabarrio.application.mapper.CategoriaDomainMapper;
import com.alertabarrio.domain.model.Categoria;
import com.alertabarrio.domain.port.in.ActualizarCategoriaUseCase;
import com.alertabarrio.domain.port.out.CategoriaRepositoryPort;

@UseCase
public class ActualizarCategoriaUseCaseImpl implements ActualizarCategoriaUseCase {

    private final CategoriaRepositoryPort categoriaRepository;
    private final CategoriaDomainMapper mapper;

    public ActualizarCategoriaUseCaseImpl(CategoriaRepositoryPort categoriaRepository, CategoriaDomainMapper mapper) {
        this.categoriaRepository = categoriaRepository;
        this.mapper = mapper;
    }

    @Override
    public CategoriaDTO execute(ActualizarCategoriaCommand command) {
        // Reconstruir con nuevos valores (simula update)
        Categoria actualizada = Categoria.reconstruir(command.id(), command.nombre(), command.iconoReferencia());
        Categoria saved = categoriaRepository.save(actualizada);
        return mapper.toDto(saved);
    }
}
