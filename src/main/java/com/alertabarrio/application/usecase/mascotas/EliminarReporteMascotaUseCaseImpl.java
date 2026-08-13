package com.alertabarrio.application.usecase.mascotas;

import com.alertabarrio.application.UseCase;
import com.alertabarrio.application.command.mascotas.EliminarReporteMascotaCommand;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.model.mascotas.ReporteMascota;
import com.alertabarrio.domain.model.mascotas.valueobject.ReporteMascotaId;
import com.alertabarrio.domain.port.in.mascotas.EliminarReporteMascotaUseCase;
import com.alertabarrio.domain.port.out.mascotas.ReporteMascotaRepositoryPort;

import java.time.Clock;

/**
 * Eliminación lógica (soft delete): el reporte pasa a estado DELETED.
 * La fila permanece en la base de datos para auditoría.
 */
@UseCase
public class EliminarReporteMascotaUseCaseImpl implements EliminarReporteMascotaUseCase {

    private final ReporteMascotaRepositoryPort reporteRepository;
    private final Clock clock;

    public EliminarReporteMascotaUseCaseImpl(ReporteMascotaRepositoryPort reporteRepository, Clock clock) {
        this.reporteRepository = reporteRepository;
        this.clock = clock;
    }

    @Override
    public void execute(EliminarReporteMascotaCommand command) {
        ReporteMascotaId id = new ReporteMascotaId(command.id());
        ReporteMascota existente = reporteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ReporteMascota", command.id()));

        ReporteMascota eliminada = existente.eliminar(clock);
        reporteRepository.save(eliminada);
    }
}
