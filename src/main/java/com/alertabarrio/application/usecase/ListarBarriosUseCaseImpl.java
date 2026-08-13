package com.alertabarrio.application.usecase;

import com.alertabarrio.application.dto.BarrioDTO;
import com.alertabarrio.application.mapper.BarrioDomainMapper;
import com.alertabarrio.application.query.ListarBarriosQuery;
import com.alertabarrio.domain.port.in.ListarBarriosUseCase;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.port.out.BarrioRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ListarBarriosUseCaseImpl implements ListarBarriosUseCase {

    private final BarrioRepositoryPort barrioRepository;
    private final BarrioDomainMapper mapper;

    public ListarBarriosUseCaseImpl(BarrioRepositoryPort barrioRepository, BarrioDomainMapper mapper) {
        this.barrioRepository = barrioRepository;
        this.mapper = mapper;
    }

    @Override
    public Pagina<BarrioDTO> execute(ListarBarriosQuery query) {
        Pagina<com.alertabarrio.domain.model.Barrio> barriosPage = query.localidadId() != null
                ? barrioRepository.findAllByLocalidadId(query.localidadId(), query.paginacion())
                : barrioRepository.findAll(query.paginacion());
        return new Pagina<>(
                barriosPage.contenido().stream().map(mapper::toDto).toList(),
                barriosPage.pagina(),
                barriosPage.tamanio(),
                barriosPage.totalElementos(),
                barriosPage.totalPaginas()
        );
    }
}
