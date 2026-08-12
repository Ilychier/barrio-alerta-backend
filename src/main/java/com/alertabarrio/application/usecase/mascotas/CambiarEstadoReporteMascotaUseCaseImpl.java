package com.alertabarrio.application.usecase.mascotas;

import com.alertabarrio.application.UseCase;
import com.alertabarrio.application.command.mascotas.CambiarEstadoReporteMascotaCommand;
import com.alertabarrio.application.dto.mascotas.ReporteMascotaDTO;
import com.alertabarrio.application.mapper.mascotas.ReporteMascotaDomainMapper;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.model.mascotas.EstadoReporte;
import com.alertabarrio.domain.model.mascotas.ReporteMascota;
import com.alertabarrio.domain.model.mascotas.valueobject.ReporteMascotaId;
import com.alertabarrio.domain.port.in.mascotas.CambiarEstadoReporteMascotaUseCase;
import com.alertabarrio.domain.port.out.mascotas.ReporteMascotaRepositoryPort;

import java.time.Clock;

@UseCase
public class CambiarEstadoReporteMascotaUseCaseImpl implements CambiarEstadoReporteMascotaUseCase {

    private final ReporteMascotaRepositoryPort reporteRepository;
    private final ReporteMascotaDomainMapper mapper;
    private final Clock clock;

    public CambiarEstadoReporteMascotaUseCaseImpl(ReporteMascotaRepositoryPort reporteRepository,
                                                  ReporteMascotaDomainMapper mapper, Clock clock) {
        this.reporteRepository = reporteRepository;
        this.mapper = mapper;
        this.clock = clock;
    }

    @Override
    public ReporteMascotaDTO execute(CambiarEstadoReporteMascotaCommand command) {
        ReporteMascotaId id = new ReporteMascotaId(command.id());
        ReporteMascota existente = reporteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ReporteMascota", command.id()));

        EstadoReporte nuevoEstado = EstadoReporte.fromString(command.estado());
        ReporteMascota actualizada = existente.cambiarEstado(nuevoEstado, clock);
        ReporteMascota saved = reporteRepository.save(actualizada);
        return mapper.toDto(saved);
    }
}
