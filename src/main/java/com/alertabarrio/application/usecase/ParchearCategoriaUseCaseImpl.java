package com.alertabarrio.application.usecase;

import com.alertabarrio.application.UseCase;
import com.alertabarrio.application.command.ParchearCategoriaCommand;
import com.alertabarrio.application.dto.CategoriaDTO;
import com.alertabarrio.application.mapper.CategoriaDomainMapper;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.model.Categoria;
import com.alertabarrio.domain.model.valueobject.CategoriaId;
import com.alertabarrio.domain.port.in.ParchearCategoriaUseCase;
import com.alertabarrio.domain.port.out.CategoriaRepositoryPort;

@UseCase
public class ParchearCategoriaUseCaseImpl implements ParchearCategoriaUseCase {

    private final CategoriaRepositoryPort categoriaRepository;
    private final CategoriaDomainMapper mapper;

    public ParchearCategoriaUseCaseImpl(CategoriaRepositoryPort categoriaRepository, CategoriaDomainMapper mapper) {
        this.categoriaRepository = categoriaRepository;
        this.mapper = mapper;
    }

    @Override
    public CategoriaDTO execute(ParchearCategoriaCommand command) {
        CategoriaId id = new CategoriaId(command.id());
        Categoria existente = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", command.id()));

        String nombre = command.nombre() != null ? command.nombre() : existente.getNombre();
        String icono = command.iconoReferencia() != null ? command.iconoReferencia() : existente.getIconoReferencia();

        Categoria parcheada = Categoria.reconstruir(command.id(), nombre, icono);
        Categoria saved = categoriaRepository.save(parcheada);
        return mapper.toDto(saved);
    }
}
