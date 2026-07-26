package com.alertabarrio.application.usecase;

import com.alertabarrio.application.dto.CategoriaDescripcionDTO;
import com.alertabarrio.application.mapper.CategoriaDescripcionDomainMapper;
import com.alertabarrio.application.query.ListarCategoriaDescripcionesQuery;
import com.alertabarrio.domain.port.in.ListarCategoriaDescripcionesUseCase;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.port.out.CategoriaDescripcionRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ListarCategoriaDescripcionesUseCaseImpl implements ListarCategoriaDescripcionesUseCase {

    private final CategoriaDescripcionRepositoryPort repository;
    private final CategoriaDescripcionDomainMapper mapper;

    public ListarCategoriaDescripcionesUseCaseImpl(CategoriaDescripcionRepositoryPort repository, CategoriaDescripcionDomainMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Pagina<CategoriaDescripcionDTO> execute(ListarCategoriaDescripcionesQuery query) {
        Pagina<com.alertabarrio.domain.model.CategoriaDescripcion> descsPage;
        if (query.categoriaId() != null) {
            descsPage = repository.findByCategoriaId(query.categoriaId(), query.paginacion());
        } else {
            descsPage = repository.findAll(query.paginacion());
        }
        return new Pagina<>(
                descsPage.contenido().stream().map(mapper::toDto).toList(),
                descsPage.pagina(),
                descsPage.tamanio(),
                descsPage.totalElementos(),
                descsPage.totalPaginas()
        );
    }
}
