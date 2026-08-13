package com.alertabarrio.application.usecase.mascotas;

import com.alertabarrio.application.dto.mascotas.ReporteMascotaDTO;
import com.alertabarrio.application.mapper.mascotas.ReporteMascotaDomainMapper;
import com.alertabarrio.application.query.mascotas.BuscarReporteMascotaQuery;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.model.mascotas.valueobject.ReporteMascotaId;
import com.alertabarrio.domain.port.in.mascotas.BuscarReporteMascotaUseCase;
import com.alertabarrio.domain.port.out.mascotas.ReporteMascotaRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BuscarReporteMascotaUseCaseImpl implements BuscarReporteMascotaUseCase {

    private final ReporteMascotaRepositoryPort reporteRepository;
    private final ReporteMascotaDomainMapper mapper;

    public BuscarReporteMascotaUseCaseImpl(ReporteMascotaRepositoryPort reporteRepository,
                                           ReporteMascotaDomainMapper mapper) {
        this.reporteRepository = reporteRepository;
        this.mapper = mapper;
    }

    @Override
    public ReporteMascotaDTO execute(BuscarReporteMascotaQuery query) {
        return reporteRepository.findById(new ReporteMascotaId(query.id()))
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("ReporteMascota", query.id()));
    }
}
