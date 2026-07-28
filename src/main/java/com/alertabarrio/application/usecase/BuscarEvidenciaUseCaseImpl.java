package com.alertabarrio.application.usecase;

import com.alertabarrio.application.dto.EvidenciaDTO;
import com.alertabarrio.application.mapper.EvidenciaDomainMapper;
import com.alertabarrio.application.query.BuscarEvidenciaQuery;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.model.valueobject.EvidenciaId;
import com.alertabarrio.domain.port.in.BuscarEvidenciaUseCase;
import com.alertabarrio.domain.port.out.EvidenciaRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BuscarEvidenciaUseCaseImpl implements BuscarEvidenciaUseCase {

    private final EvidenciaRepositoryPort evidenciaRepository;
    private final EvidenciaDomainMapper mapper;

    public BuscarEvidenciaUseCaseImpl(EvidenciaRepositoryPort evidenciaRepository, EvidenciaDomainMapper mapper) {
        this.evidenciaRepository = evidenciaRepository;
        this.mapper = mapper;
    }

    @Override
    public EvidenciaDTO execute(BuscarEvidenciaQuery query) {
        return evidenciaRepository.findById(new EvidenciaId(query.id()))
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Evidencia", query.id()));
    }
}
