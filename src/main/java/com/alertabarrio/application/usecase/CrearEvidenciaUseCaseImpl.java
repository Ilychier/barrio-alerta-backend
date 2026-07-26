package com.alertabarrio.application.usecase;

import com.alertabarrio.application.UseCase;
import com.alertabarrio.application.command.CrearEvidenciaCommand;
import com.alertabarrio.application.dto.EvidenciaDTO;
import com.alertabarrio.application.mapper.EvidenciaDomainMapper;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.model.Evidencia;
import com.alertabarrio.domain.model.valueobject.AlertaId;
import com.alertabarrio.domain.port.in.CrearEvidenciaUseCase;
import com.alertabarrio.domain.port.out.AlertaRepositoryPort;
import com.alertabarrio.domain.port.out.EvidenciaRepositoryPort;

import java.time.Clock;

@UseCase
public class CrearEvidenciaUseCaseImpl implements CrearEvidenciaUseCase {

    private final EvidenciaRepositoryPort evidenciaRepository;
    private final AlertaRepositoryPort alertaRepository;
    private final EvidenciaDomainMapper mapper;
    private final Clock clock;

    public CrearEvidenciaUseCaseImpl(EvidenciaRepositoryPort evidenciaRepository,
                                     AlertaRepositoryPort alertaRepository,
                                     EvidenciaDomainMapper mapper,
                                     Clock clock) {
        this.evidenciaRepository = evidenciaRepository;
        this.alertaRepository = alertaRepository;
        this.mapper = mapper;
        this.clock = clock;
    }

    @Override
    public EvidenciaDTO execute(CrearEvidenciaCommand command) {
        if (!alertaRepository.existsById(new AlertaId(command.alertaId()))) {
            throw new ResourceNotFoundException("Alerta", command.alertaId());
        }

        Evidencia evidencia = Evidencia.crear(command.archivoUrl(), command.alertaId(), clock);
        Evidencia saved = evidenciaRepository.save(evidencia);
        return mapper.toDto(saved);
    }
}
