package com.alertabarrio.application.usecase;

import com.alertabarrio.application.dto.ConfiguracionDTO;
import com.alertabarrio.application.mapper.ConfiguracionDomainMapper;
import com.alertabarrio.application.query.ObtenerConfiguracionQuery;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.model.valueobject.ConfiguracionId;
import com.alertabarrio.domain.port.in.ObtenerConfiguracionUseCase;
import com.alertabarrio.domain.port.out.ConfiguracionRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ObtenerConfiguracionUseCaseImpl implements ObtenerConfiguracionUseCase {

    private final ConfiguracionRepositoryPort configuracionRepository;
    private final ConfiguracionDomainMapper mapper;

    public ObtenerConfiguracionUseCaseImpl(ConfiguracionRepositoryPort configuracionRepository, ConfiguracionDomainMapper mapper) {
        this.configuracionRepository = configuracionRepository;
        this.mapper = mapper;
    }

    @Override
    public ConfiguracionDTO execute(ObtenerConfiguracionQuery query) {
        return configuracionRepository.findById(new ConfiguracionId(query.id()))
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Configuracion", query.id()));
    }
}
