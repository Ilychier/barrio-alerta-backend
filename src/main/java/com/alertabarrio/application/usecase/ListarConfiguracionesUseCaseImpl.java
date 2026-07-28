package com.alertabarrio.application.usecase;

import com.alertabarrio.application.dto.ConfiguracionDTO;
import com.alertabarrio.application.mapper.ConfiguracionDomainMapper;
import com.alertabarrio.application.query.ListarConfiguracionesQuery;
import com.alertabarrio.domain.port.in.ListarConfiguracionesUseCase;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.port.out.ConfiguracionRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ListarConfiguracionesUseCaseImpl implements ListarConfiguracionesUseCase {

    private final ConfiguracionRepositoryPort configuracionRepository;
    private final ConfiguracionDomainMapper mapper;

    public ListarConfiguracionesUseCaseImpl(ConfiguracionRepositoryPort configuracionRepository, ConfiguracionDomainMapper mapper) {
        this.configuracionRepository = configuracionRepository;
        this.mapper = mapper;
    }

    @Override
    public Pagina<ConfiguracionDTO> execute(ListarConfiguracionesQuery query) {
        Pagina<com.alertabarrio.domain.model.Configuracion> configsPage = configuracionRepository.findAll(query.paginacion());
        return new Pagina<>(
                configsPage.contenido().stream().map(mapper::toDto).toList(),
                configsPage.pagina(),
                configsPage.tamanio(),
                configsPage.totalElementos(),
                configsPage.totalPaginas()
        );
    }
}
