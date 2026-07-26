package com.alertabarrio.application.usecase;

import com.alertabarrio.application.dto.CategoriaDTO;
import com.alertabarrio.application.mapper.CategoriaDomainMapper;
import com.alertabarrio.application.query.ListarCategoriasQuery;
import com.alertabarrio.domain.port.in.ListarCategoriasUseCase;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.port.out.CategoriaRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ListarCategoriasUseCaseImpl implements ListarCategoriasUseCase {

    private final CategoriaRepositoryPort categoriaRepository;
    private final CategoriaDomainMapper mapper;

    public ListarCategoriasUseCaseImpl(CategoriaRepositoryPort categoriaRepository, CategoriaDomainMapper mapper) {
        this.categoriaRepository = categoriaRepository;
        this.mapper = mapper;
    }

    @Override
    public Pagina<CategoriaDTO> execute(ListarCategoriasQuery query) {
        Pagina<com.alertabarrio.domain.model.Categoria> categoriasPage = categoriaRepository.findAll(query.paginacion());
        return new Pagina<>(
                categoriasPage.contenido().stream().map(mapper::toDto).toList(),
                categoriasPage.pagina(),
                categoriasPage.tamanio(),
                categoriasPage.totalElementos(),
                categoriasPage.totalPaginas()
        );
    }
}
