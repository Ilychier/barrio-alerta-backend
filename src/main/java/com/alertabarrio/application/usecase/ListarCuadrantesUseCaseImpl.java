package com.alertabarrio.application.usecase;

import com.alertabarrio.application.dto.CuadranteDTO;
import com.alertabarrio.application.mapper.CuadranteDomainMapper;
import com.alertabarrio.application.query.ListarCuadrantesQuery;
import com.alertabarrio.domain.port.in.ListarCuadrantesUseCase;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.port.out.CuadranteRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ListarCuadrantesUseCaseImpl implements ListarCuadrantesUseCase {

    private final CuadranteRepositoryPort cuadranteRepository;
    private final CuadranteDomainMapper mapper;

    public ListarCuadrantesUseCaseImpl(CuadranteRepositoryPort cuadranteRepository, CuadranteDomainMapper mapper) {
        this.cuadranteRepository = cuadranteRepository;
        this.mapper = mapper;
    }

    @Override
    public Pagina<CuadranteDTO> execute(ListarCuadrantesQuery query) {
        Pagina<com.alertabarrio.domain.model.Cuadrante> cuadrantesPage = cuadranteRepository.findAll(query.paginacion());
        return new Pagina<>(
                cuadrantesPage.contenido().stream().map(mapper::toDto).toList(),
                cuadrantesPage.pagina(),
                cuadrantesPage.tamanio(),
                cuadrantesPage.totalElementos(),
                cuadrantesPage.totalPaginas()
        );
    }
}
