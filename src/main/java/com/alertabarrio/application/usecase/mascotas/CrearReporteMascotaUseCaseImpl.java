package com.alertabarrio.application.usecase.mascotas;

import com.alertabarrio.application.UseCase;
import com.alertabarrio.application.command.mascotas.CrearReporteMascotaCommand;
import com.alertabarrio.application.dto.mascotas.ReporteMascotaDTO;
import com.alertabarrio.application.mapper.mascotas.ReporteMascotaDomainMapper;
import com.alertabarrio.domain.model.mascotas.ReporteMascota;
import com.alertabarrio.domain.port.in.mascotas.CrearReporteMascotaUseCase;
import com.alertabarrio.domain.port.out.mascotas.ReporteMascotaRepositoryPort;

import java.time.Clock;

@UseCase
public class CrearReporteMascotaUseCaseImpl implements CrearReporteMascotaUseCase {

    private final ReporteMascotaRepositoryPort reporteRepository;
    private final ReporteMascotaDomainMapper mapper;
    private final Clock clock;

    public CrearReporteMascotaUseCaseImpl(ReporteMascotaRepositoryPort reporteRepository,
                                          ReporteMascotaDomainMapper mapper, Clock clock) {
        this.reporteRepository = reporteRepository;
        this.mapper = mapper;
        this.clock = clock;
    }

    @Override
    public ReporteMascotaDTO execute(CrearReporteMascotaCommand command) {
        ReporteMascota reporte = ReporteMascota.crear(
                command.tipoReporte(),
                command.tipoMascotaId(),
                command.ciudadId(),
                command.ubicacion(),
                command.telefono(),
                command.descripcion(),
                command.usuarioId(),
                clock);
        ReporteMascota saved = reporteRepository.save(reporte);
        return mapper.toDto(saved);
    }
}
