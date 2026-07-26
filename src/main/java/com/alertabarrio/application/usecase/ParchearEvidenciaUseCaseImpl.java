package com.alertabarrio.application.usecase;

import com.alertabarrio.application.UseCase;
import com.alertabarrio.application.command.ParchearEvidenciaCommand;
import com.alertabarrio.application.dto.EvidenciaDTO;
import com.alertabarrio.application.mapper.EvidenciaDomainMapper;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.model.Evidencia;
import com.alertabarrio.domain.model.valueobject.AlertaId;
import com.alertabarrio.domain.model.valueobject.EvidenciaId;
import com.alertabarrio.domain.port.in.ParchearEvidenciaUseCase;
import com.alertabarrio.domain.port.out.AlertaRepositoryPort;
import com.alertabarrio.domain.port.out.EvidenciaRepositoryPort;

@UseCase
public class ParchearEvidenciaUseCaseImpl implements ParchearEvidenciaUseCase {

    private final EvidenciaRepositoryPort evidenciaRepository;
    private final AlertaRepositoryPort alertaRepository;
    private final EvidenciaDomainMapper mapper;

    public ParchearEvidenciaUseCaseImpl(EvidenciaRepositoryPort evidenciaRepository,
                                        AlertaRepositoryPort alertaRepository,
                                        EvidenciaDomainMapper mapper) {
        this.evidenciaRepository = evidenciaRepository;
        this.alertaRepository = alertaRepository;
        this.mapper = mapper;
    }

    @Override
    public EvidenciaDTO execute(ParchearEvidenciaCommand command) {
        EvidenciaId id = new EvidenciaId(command.id());
        Evidencia existente = evidenciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evidencia", command.id()));

        String archivoUrl = command.archivoUrl() != null ? command.archivoUrl() : existente.getArchivoUrl();
        Long alertaId = command.alertaId() != null ? command.alertaId() : existente.getAlertaId().value();

        if (command.alertaId() != null && !alertaRepository.existsById(new AlertaId(command.alertaId()))) {
            throw new ResourceNotFoundException("Alerta", command.alertaId());
        }

        Evidencia parcheada = Evidencia.reconstruir(
                command.id(),
                archivoUrl,
                existente.getFechaSubida(),
                alertaId
        );
        Evidencia saved = evidenciaRepository.save(parcheada);
        return mapper.toDto(saved);
    }
}
