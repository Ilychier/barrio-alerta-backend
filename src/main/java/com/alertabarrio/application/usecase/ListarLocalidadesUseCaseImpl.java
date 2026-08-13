package com.alertabarrio.application.usecase;

import com.alertabarrio.application.dto.LocalidadDTO;
import com.alertabarrio.application.mapper.LocalidadDomainMapper;
import com.alertabarrio.application.query.ListarLocalidadesQuery;
import com.alertabarrio.domain.model.Localidad;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.port.in.ListarLocalidadesUseCase;
import com.alertabarrio.domain.port.out.LocalidadRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ListarLocalidadesUseCaseImpl implements ListarLocalidadesUseCase {

    private final LocalidadRepositoryPort localidadRepository;
    private final LocalidadDomainMapper mapper;

    public ListarLocalidadesUseCaseImpl(LocalidadRepositoryPort localidadRepository, LocalidadDomainMapper mapper) {
        this.localidadRepository = localidadRepository;
        this.mapper = mapper;
    }

    @Override
    public Pagina<LocalidadDTO> execute(ListarLocalidadesQuery query) {
        Pagina<Localidad> localidades = localidadRepository.findByMunicipioId(query.municipioId(), query.paginacion());
        return new Pagina<>(
                localidades.contenido().stream().map(mapper::toDto).toList(),
                localidades.pagina(),
                localidades.tamanio(),
                localidades.totalElementos(),
                localidades.totalPaginas()
        );
    }
}
