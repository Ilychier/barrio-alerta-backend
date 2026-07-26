package com.alertabarrio.application.usecase;

import com.alertabarrio.application.dto.CategoriaDescripcionDTO;
import com.alertabarrio.application.mapper.CategoriaDescripcionDomainMapper;
import com.alertabarrio.application.query.BuscarCategoriaDescripcionQuery;
import com.alertabarrio.domain.model.valueobject.CategoriaDescripcionId;
import com.alertabarrio.domain.port.in.BuscarCategoriaDescripcionUseCase;
import com.alertabarrio.domain.port.out.CategoriaDescripcionRepositoryPort;
import java.util.Optional;

public class BuscarCategoriaDescripcionUseCaseImpl implements BuscarCategoriaDescripcionUseCase {

    private final CategoriaDescripcionRepositoryPort repository;
    private final CategoriaDescripcionDomainMapper mapper;

    public BuscarCategoriaDescripcionUseCaseImpl(CategoriaDescripcionRepositoryPort repository, CategoriaDescripcionDomainMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<CategoriaDescripcionDTO> execute(BuscarCategoriaDescripcionQuery query) {
        return repository.findById(new CategoriaDescripcionId(query.id()))
                .map(mapper::toDto);
    }
}
