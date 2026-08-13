package com.alertabarrio.application.usecase.mascotas;

import com.alertabarrio.application.dto.mascotas.ReporteMascotaDTO;
import com.alertabarrio.application.mapper.mascotas.ReporteMascotaDomainMapper;
import com.alertabarrio.domain.model.mascotas.ReporteMascota;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.model.valueobject.Paginacion;
import com.alertabarrio.domain.model.valueobject.UsuarioId;
import com.alertabarrio.domain.port.in.mascotas.ListarMisReportesMascotaUseCase;
import com.alertabarrio.domain.port.out.mascotas.ReporteMascotaRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Mis reportes: reportes creados por un usuario autenticado.
 * Incluye reportes con estado DELETED (el usuario ve su historial completo).
 */
@Service
@Transactional(readOnly = true)
public class ListarMisReportesMascotaUseCaseImpl implements ListarMisReportesMascotaUseCase {

    private final ReporteMascotaRepositoryPort reporteRepository;
    private final ReporteMascotaDomainMapper mapper;

    public ListarMisReportesMascotaUseCaseImpl(ReporteMascotaRepositoryPort reporteRepository,
                                               ReporteMascotaDomainMapper mapper) {
        this.reporteRepository = reporteRepository;
        this.mapper = mapper;
    }

    @Override
    public Pagina<ReporteMascotaDTO> execute(Long usuarioId, Paginacion paginacion) {
        Pagina<ReporteMascota> pagina = reporteRepository.findByUsuarioId(new UsuarioId(usuarioId), paginacion);
        return new Pagina<>(
                pagina.contenido().stream().map(mapper::toDto).toList(),
                pagina.pagina(),
                pagina.tamanio(),
                pagina.totalElementos(),
                pagina.totalPaginas()
        );
    }
}
