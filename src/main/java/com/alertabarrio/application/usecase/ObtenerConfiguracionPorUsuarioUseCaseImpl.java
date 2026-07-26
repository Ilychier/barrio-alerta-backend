package com.alertabarrio.application.usecase;

import com.alertabarrio.application.dto.ConfiguracionDTO;
import com.alertabarrio.application.mapper.ConfiguracionDomainMapper;
import com.alertabarrio.application.query.ObtenerConfiguracionPorUsuarioQuery;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.port.in.ObtenerConfiguracionPorUsuarioUseCase;
import com.alertabarrio.domain.port.out.ConfiguracionRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ObtenerConfiguracionPorUsuarioUseCaseImpl implements ObtenerConfiguracionPorUsuarioUseCase {

    private final ConfiguracionRepositoryPort configuracionRepository;
    private final ConfiguracionDomainMapper mapper;

    public ObtenerConfiguracionPorUsuarioUseCaseImpl(ConfiguracionRepositoryPort configuracionRepository, ConfiguracionDomainMapper mapper) {
        this.configuracionRepository = configuracionRepository;
        this.mapper = mapper;
    }

    @Override
    public ConfiguracionDTO execute(ObtenerConfiguracionPorUsuarioQuery query) {
        return configuracionRepository.findByUsuarioId(query.usuarioId())
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Configuracion", query.usuarioId()));
    }
}
