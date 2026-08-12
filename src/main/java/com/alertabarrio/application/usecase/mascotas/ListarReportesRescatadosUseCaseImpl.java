package com.alertabarrio.application.usecase.mascotas;

import com.alertabarrio.application.dto.mascotas.ReporteMascotaDTO;
import com.alertabarrio.application.mapper.mascotas.ReporteMascotaDomainMapper;
import com.alertabarrio.domain.model.mascotas.EstadoReporte;
import com.alertabarrio.domain.model.mascotas.ReporteMascota;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.model.valueobject.Paginacion;
import com.alertabarrio.domain.port.in.mascotas.ListarReportesRescatadosUseCase;
import com.alertabarrio.domain.port.out.mascotas.ReporteMascotaRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Historias de rescate: feed de reportes con estado RESCUED.
 */
@Service
@Transactional(readOnly = true)
public class ListarReportesRescatadosUseCaseImpl implements ListarReportesRescatadosUseCase {

    private final ReporteMascotaRepositoryPort reporteRepository;
    private final ReporteMascotaDomainMapper mapper;

    public ListarReportesRescatadosUseCaseImpl(ReporteMascotaRepositoryPort reporteRepository,
                                               ReporteMascotaDomainMapper mapper) {
        this.reporteRepository = reporteRepository;
        this.mapper = mapper;
    }

    @Override
    public Pagina<ReporteMascotaDTO> execute(Paginacion paginacion) {
        Pagina<ReporteMascota> pagina = reporteRepository.findByEstado(EstadoReporte.RESCUED, paginacion);
        return new Pagina<>(
                pagina.contenido().stream().map(mapper::toDto).toList(),
                pagina.pagina(),
                pagina.tamanio(),
                pagina.totalElementos(),
                pagina.totalPaginas()
        );
    }
}
