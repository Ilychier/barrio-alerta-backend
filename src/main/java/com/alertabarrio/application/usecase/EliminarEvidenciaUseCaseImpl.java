package com.alertabarrio.application.usecase;

import com.alertabarrio.application.UseCase;
import com.alertabarrio.application.command.EliminarEvidenciaCommand;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.model.valueobject.EvidenciaId;
import com.alertabarrio.domain.port.in.EliminarEvidenciaUseCase;
import com.alertabarrio.domain.port.out.EvidenciaRepositoryPort;

@UseCase
public class EliminarEvidenciaUseCaseImpl implements EliminarEvidenciaUseCase {

    private final EvidenciaRepositoryPort evidenciaRepository;

    public EliminarEvidenciaUseCaseImpl(EvidenciaRepositoryPort evidenciaRepository) {
        this.evidenciaRepository = evidenciaRepository;
    }

    @Override
    public void execute(EliminarEvidenciaCommand command) {
        EvidenciaId id = new EvidenciaId(command.id());
        if (!evidenciaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Evidencia", command.id());
        }
        evidenciaRepository.deleteById(id);
    }
}
