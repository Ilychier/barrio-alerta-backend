package com.alertabarrio.application.usecase;

import com.alertabarrio.application.UseCase;
import com.alertabarrio.application.command.ParchearCategoriaDescripcionCommand;
import com.alertabarrio.application.dto.CategoriaDescripcionDTO;
import com.alertabarrio.application.mapper.CategoriaDescripcionDomainMapper;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.model.CategoriaDescripcion;
import com.alertabarrio.domain.model.valueobject.CategoriaDescripcionId;
import com.alertabarrio.domain.port.in.ParchearCategoriaDescripcionUseCase;
import com.alertabarrio.domain.port.out.CategoriaDescripcionRepositoryPort;

@UseCase
public class ParchearCategoriaDescripcionUseCaseImpl implements ParchearCategoriaDescripcionUseCase {

    private final CategoriaDescripcionRepositoryPort repository;
    private final CategoriaDescripcionDomainMapper mapper;

    public ParchearCategoriaDescripcionUseCaseImpl(CategoriaDescripcionRepositoryPort repository, CategoriaDescripcionDomainMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public CategoriaDescripcionDTO execute(ParchearCategoriaDescripcionCommand command) {
        CategoriaDescripcionId id = new CategoriaDescripcionId(command.id());
        CategoriaDescripcion existente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CategoriaDescripcion", command.id()));

        String descripcion = command.descripcion() != null ? command.descripcion() : existente.getDescripcion();
        Long categoriaId = command.categoriaId() != null ? command.categoriaId() : existente.getCategoriaId().value();
        String imagenUrl = command.imagenUrl() != null ? command.imagenUrl() : existente.getImagenUrl();

        CategoriaDescripcion parcheada = CategoriaDescripcion.reconstruir(command.id(), descripcion, categoriaId, imagenUrl);
        CategoriaDescripcion saved = repository.save(parcheada);
        return mapper.toDto(saved);
    }
}
