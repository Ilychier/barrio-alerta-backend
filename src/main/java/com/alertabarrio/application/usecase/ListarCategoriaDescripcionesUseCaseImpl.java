package com.alertabarrio.application.usecase;

import com.alertabarrio.application.dto.CategoriaDescripcionDTO;
import com.alertabarrio.application.mapper.CategoriaDescripcionDomainMapper;
import com.alertabarrio.application.query.ListarCategoriaDescripcionesQuery;
import com.alertabarrio.domain.port.in.ListarCategoriaDescripcionesUseCase;
import com.alertabarrio.domain.port.out.CategoriaDescripcionRepositoryPort;
import org.springframework.data.domain.Page;

public class ListarCategoriaDescripcionesUseCaseImpl implements ListarCategoriaDescripcionesUseCase {

    private final CategoriaDescripcionRepositoryPort repository;
    private final CategoriaDescripcionDomainMapper mapper;

    public ListarCategoriaDescripcionesUseCaseImpl(CategoriaDescripcionRepositoryPort repository, CategoriaDescripcionDomainMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Page<CategoriaDescripcionDTO> execute(ListarCategoriaDescripcionesQuery query) {
        if (query.categoriaId() != null) {
            return repository.findByCategoriaId(query.categoriaId(), query.pageable())
                    .map(mapper::toDto);
        }
        return repository.findAll(query.pageable())
                .map(mapper::toDto);
    }
}
