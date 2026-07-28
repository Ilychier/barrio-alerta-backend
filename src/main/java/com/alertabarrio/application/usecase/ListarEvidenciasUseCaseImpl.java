package com.alertabarrio.application.usecase;

import com.alertabarrio.application.dto.EvidenciaDTO;
import com.alertabarrio.application.mapper.EvidenciaDomainMapper;
import com.alertabarrio.application.query.ListarEvidenciasQuery;
import com.alertabarrio.domain.port.in.ListarEvidenciasUseCase;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.port.out.EvidenciaRepositoryPort;
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
    public Pagina<EvidenciaDTO> execute(ListarEvidenciasQuery query) {
        Pagina<com.alertabarrio.domain.model.Evidencia> evidenciasPage;
        if (query.alertaId() != null) {
            evidenciasPage = evidenciaRepository.findByAlertaId(query.alertaId(), query.paginacion());
        } else {
            evidenciasPage = evidenciaRepository.findAll(query.paginacion());
        }
        return new Pagina<>(
                evidenciasPage.contenido().stream().map(mapper::toDto).toList(),
                evidenciasPage.pagina(),
                evidenciasPage.tamanio(),
                evidenciasPage.totalElementos(),
                evidenciasPage.totalPaginas()
        );
    }
}
