package com.alertabarrio.application.usecase;

import com.alertabarrio.application.UseCase;
import com.alertabarrio.application.command.ActualizarEvidenciaCommand;
import com.alertabarrio.application.dto.EvidenciaDTO;
import com.alertabarrio.application.mapper.EvidenciaDomainMapper;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.model.Evidencia;
import com.alertabarrio.domain.model.valueobject.AlertaId;
import com.alertabarrio.domain.model.valueobject.EvidenciaId;
import com.alertabarrio.domain.port.in.ActualizarEvidenciaUseCase;
import com.alertabarrio.domain.port.out.AlertaRepositoryPort;
import com.alertabarrio.domain.port.out.EvidenciaRepositoryPort;

@UseCase
public class ActualizarEvidenciaUseCaseImpl implements ActualizarEvidenciaUseCase {

    private final EvidenciaRepositoryPort evidenciaRepository;
    private final AlertaRepositoryPort alertaRepository;
    private final EvidenciaDomainMapper mapper;

    public ActualizarEvidenciaUseCaseImpl(EvidenciaRepositoryPort evidenciaRepository,
                                          AlertaRepositoryPort alertaRepository,
                                          EvidenciaDomainMapper mapper) {
        this.evidenciaRepository = evidenciaRepository;
        this.alertaRepository = alertaRepository;
        this.mapper = mapper;
    }

    @Override
    public EvidenciaDTO execute(ActualizarEvidenciaCommand command) {
        EvidenciaId id = new EvidenciaId(command.id());
        Evidencia existente = evidenciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evidencia", command.id()));

        if (!alertaRepository.existsById(new AlertaId(command.alertaId()))) {
            throw new ResourceNotFoundException("Alerta", command.alertaId());
        }

        Evidencia actualizada = Evidencia.reconstruir(
                command.id(),
                command.archivoUrl(),
                existente.getFechaSubida(),
                command.alertaId()
        );
        Evidencia saved = evidenciaRepository.save(actualizada);
        return mapper.toDto(saved);
    }
}
