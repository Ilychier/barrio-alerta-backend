package com.alertabarrio.application.usecase;

import com.alertabarrio.application.UseCase;
import com.alertabarrio.application.command.CrearCategoriaCommand;
import com.alertabarrio.application.dto.CategoriaDTO;
import com.alertabarrio.application.mapper.CategoriaDomainMapper;
import com.alertabarrio.domain.model.Categoria;
import com.alertabarrio.domain.port.in.CrearCategoriaUseCase;
import com.alertabarrio.domain.port.out.CategoriaRepositoryPort;

@UseCase
public class CrearCategoriaUseCaseImpl implements CrearCategoriaUseCase {

    private final CategoriaRepositoryPort categoriaRepository;
    private final CategoriaDomainMapper mapper;

    public CrearCategoriaUseCaseImpl(CategoriaRepositoryPort categoriaRepository, CategoriaDomainMapper mapper) {
        this.categoriaRepository = categoriaRepository;
        this.mapper = mapper;
    }

    @Override
    public CategoriaDTO execute(CrearCategoriaCommand command) {
        Categoria categoria = Categoria.crear(command.nombre(), command.iconoReferencia());
        Categoria saved = categoriaRepository.save(categoria);
        return mapper.toDto(saved);
    }
}
