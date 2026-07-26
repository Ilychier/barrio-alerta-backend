package com.alertabarrio.application.usecase;

import com.alertabarrio.application.dto.ConfiguracionDTO;
import com.alertabarrio.application.mapper.ConfiguracionDomainMapper;
import com.alertabarrio.application.query.ListarConfiguracionesQuery;
import com.alertabarrio.domain.port.in.ListarConfiguracionesUseCase;
import com.alertabarrio.domain.port.out.ConfiguracionRepositoryPort;
import org.springframework.data.domain.Page;
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
    public Page<ConfiguracionDTO> execute(ListarConfiguracionesQuery query) {
        return configuracionRepository.findAll(query.pageable())
                .map(mapper::toDto);
    }
}
