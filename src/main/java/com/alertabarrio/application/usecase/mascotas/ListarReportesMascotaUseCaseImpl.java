package com.alertabarrio.application.usecase.mascotas;

import com.alertabarrio.application.dto.mascotas.ReporteMascotaDTO;
import com.alertabarrio.application.mapper.mascotas.ReporteMascotaDomainMapper;
import com.alertabarrio.application.query.mascotas.ListarReportesMascotaQuery;
import com.alertabarrio.domain.model.mascotas.EstadoReporte;
import com.alertabarrio.domain.model.mascotas.ReporteMascota;
import com.alertabarrio.domain.model.mascotas.TipoReporte;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.port.in.mascotas.ListarReportesMascotaUseCase;
import com.alertabarrio.domain.port.out.mascotas.ReporteMascotaRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Feed público de reportes de mascotas.
 * <p>
 * Filtros opcionales (null = no filtrar):
 * <ul>
 *   <li>{@code estado}: ACTIVE | RESCUED | DELETED</li>
 *   <li>{@code tipoReporte}: LOST | FOUND</li>
 *   <li>{@code ciudadId}: id de la ciudad</li>
 * </ul>
 * <p>
 * Si no se pasa ningún filtro, devuelve todos los reportes no eliminados
 * (el adapter filtra DELETED por defecto — ver port out).
 */
@Service
@Transactional(readOnly = true)
public class ListarReportesMascotaUseCaseImpl implements ListarReportesMascotaUseCase {

    private final ReporteMascotaRepositoryPort reporteRepository;
    private final ReporteMascotaDomainMapper mapper;

    public ListarReportesMascotaUseCaseImpl(ReporteMascotaRepositoryPort reporteRepository,
                                            ReporteMascotaDomainMapper mapper) {
        this.reporteRepository = reporteRepository;
        this.mapper = mapper;
    }

    @Override
    public Pagina<ReporteMascotaDTO> execute(ListarReportesMascotaQuery query) {
        EstadoReporte estado = query.estado() != null ? EstadoReporte.fromString(query.estado()) : null;
        TipoReporte tipoReporte = query.tipoReporte() != null ? TipoReporte.fromString(query.tipoReporte()) : null;

        Pagina<ReporteMascota> pagina = reporteRepository.findByFilters(
                estado, tipoReporte, query.ciudadId(), query.paginacion());

        return new Pagina<>(
                pagina.contenido().stream().map(mapper::toDto).toList(),
                pagina.pagina(),
                pagina.tamanio(),
                pagina.totalElementos(),
                pagina.totalPaginas()
        );
    }
}
