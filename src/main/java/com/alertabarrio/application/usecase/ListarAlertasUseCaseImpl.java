package com.alertabarrio.application.usecase;

import com.alertabarrio.application.dto.AlertaDTO;
import com.alertabarrio.application.mapper.AlertaDomainMapper;
import com.alertabarrio.application.query.ListarAlertasQuery;
import com.alertabarrio.domain.port.in.ListarAlertasUseCase;
import com.alertabarrio.domain.port.out.AlertaRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@Transactional(readOnly = true)
public class ListarAlertasUseCaseImpl implements ListarAlertasUseCase {

    private final AlertaRepositoryPort alertaRepository;
    private final AlertaDomainMapper mapper;

    public ListarAlertasUseCaseImpl(AlertaRepositoryPort alertaRepository, AlertaDomainMapper mapper) {
        this.alertaRepository = alertaRepository;
        this.mapper = mapper;
    }

    @Override
    public Page<AlertaDTO> execute(ListarAlertasQuery query) {
        LocalDate fecha = query.fecha();
        Long barrioId = query.barrioId();

        if (fecha != null) {
            LocalDateTime inicio = fecha.atStartOfDay();
            LocalDateTime fin = fecha.atTime(LocalTime.MAX);

            if (barrioId != null) {
                return alertaRepository.findByUsuario_Barrio_IdAndFechaHoraBetween(barrioId, inicio, fin, query.pageable())
                        .map(mapper::toDto);
            }
            return alertaRepository.findByFechaHoraBetween(inicio, fin, query.pageable())
                    .map(mapper::toDto);
        }

        return alertaRepository.findAll(query.pageable())
                .map(mapper::toDto);
    }
}
