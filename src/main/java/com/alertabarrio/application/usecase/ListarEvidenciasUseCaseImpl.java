package com.alertabarrio.application.usecase;

import com.alertabarrio.application.dto.EvidenciaDTO;
import com.alertabarrio.application.mapper.EvidenciaDomainMapper;
import com.alertabarrio.application.query.ListarEvidenciasQuery;
import com.alertabarrio.domain.port.in.ListarEvidenciasUseCase;
import com.alertabarrio.domain.port.out.EvidenciaRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ListarEvidenciasUseCaseImpl implements ListarEvidenciasUseCase {

    private final EvidenciaRepositoryPort evidenciaRepository;
    private final EvidenciaDomainMapper mapper;

    public ListarEvidenciasUseCaseImpl(EvidenciaRepositoryPort evidenciaRepository, EvidenciaDomainMapper mapper) {
        this.evidenciaRepository = evidenciaRepository;
        this.mapper = mapper;
    }

    @Override
    public Page<EvidenciaDTO> execute(ListarEvidenciasQuery query) {
        if (query.alertaId() != null) {
            return evidenciaRepository.findByAlertaId(query.alertaId(), query.pageable())
                    .map(mapper::toDto);
        }
        return evidenciaRepository.findAll(query.pageable())
                .map(mapper::toDto);
    }
}
