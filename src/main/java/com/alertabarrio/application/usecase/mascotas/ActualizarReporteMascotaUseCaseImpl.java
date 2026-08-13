package com.alertabarrio.application.usecase.mascotas;

import com.alertabarrio.application.UseCase;
import com.alertabarrio.application.command.mascotas.ActualizarReporteMascotaCommand;
import com.alertabarrio.application.dto.mascotas.ReporteMascotaDTO;
import com.alertabarrio.application.mapper.mascotas.ReporteMascotaDomainMapper;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.model.mascotas.ReporteMascota;
import com.alertabarrio.domain.model.mascotas.valueobject.ReporteMascotaId;
import com.alertabarrio.domain.port.in.mascotas.ActualizarReporteMascotaUseCase;
import com.alertabarrio.domain.port.out.mascotas.ReporteMascotaRepositoryPort;

import java.time.Clock;

@UseCase
public class ActualizarReporteMascotaUseCaseImpl implements ActualizarReporteMascotaUseCase {

    private final ReporteMascotaRepositoryPort reporteRepository;
    private final ReporteMascotaDomainMapper mapper;
    private final Clock clock;

    public ActualizarReporteMascotaUseCaseImpl(ReporteMascotaRepositoryPort reporteRepository,
                                               ReporteMascotaDomainMapper mapper, Clock clock) {
        this.reporteRepository = reporteRepository;
        this.mapper = mapper;
        this.clock = clock;
    }

    @Override
    public ReporteMascotaDTO execute(ActualizarReporteMascotaCommand command) {
        ReporteMascotaId id = new ReporteMascotaId(command.id());
        ReporteMascota existente = reporteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ReporteMascota", command.id()));

        ReporteMascota actualizada = existente.actualizar(
                command.ubicacion(), command.telefono(), command.descripcion(), clock);
        ReporteMascota saved = reporteRepository.save(actualizada);
        return mapper.toDto(saved);
    }
}
