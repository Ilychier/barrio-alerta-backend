package com.alertabarrio.application.usecase;

import com.alertabarrio.application.dto.AlertaDTO;
import com.alertabarrio.application.mapper.AlertaDomainMapper;
import com.alertabarrio.application.query.BuscarAlertaQuery;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.model.valueobject.AlertaId;
import com.alertabarrio.domain.port.in.BuscarAlertaUseCase;
import com.alertabarrio.domain.port.out.AlertaRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BuscarAlertaUseCaseImpl implements BuscarAlertaUseCase {

    private final AlertaRepositoryPort alertaRepository;
    private final AlertaDomainMapper mapper;

    public BuscarAlertaUseCaseImpl(AlertaRepositoryPort alertaRepository, AlertaDomainMapper mapper) {
        this.alertaRepository = alertaRepository;
        this.mapper = mapper;
    }

    @Override
    public AlertaDTO execute(BuscarAlertaQuery query) {
        return alertaRepository.findById(new AlertaId(query.id()))
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Alerta", query.id()));
    }
}
