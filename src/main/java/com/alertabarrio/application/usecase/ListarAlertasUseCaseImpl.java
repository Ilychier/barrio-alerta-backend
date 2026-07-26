package com.alertabarrio.application.usecase;

import com.alertabarrio.application.dto.AlertaDTO;
import com.alertabarrio.application.mapper.AlertaDomainMapper;
import com.alertabarrio.application.query.ListarAlertasQuery;
import com.alertabarrio.domain.port.in.ListarAlertasUseCase;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.port.out.AlertaRepositoryPort;
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
    public Pagina<AlertaDTO> execute(ListarAlertasQuery query) {
        LocalDate fecha = query.fecha();
        Long barrioId = query.barrioId();

        Pagina<com.alertabarrio.domain.model.Alerta> alertasPage;

        if (fecha != null) {
            LocalDateTime inicio = fecha.atStartOfDay();
            LocalDateTime fin = fecha.atTime(LocalTime.MAX);

            if (barrioId != null) {
                alertasPage = alertaRepository.findByUsuario_Barrio_IdAndFechaHoraBetween(barrioId, inicio, fin, query.paginacion());
            } else {
                alertasPage = alertaRepository.findByFechaHoraBetween(inicio, fin, query.paginacion());
            }
        } else {
            alertasPage = alertaRepository.findAll(query.paginacion());
        }

        return new Pagina<>(
                alertasPage.contenido().stream().map(mapper::toDto).toList(),
                alertasPage.pagina(),
                alertasPage.tamanio(),
                alertasPage.totalElementos(),
                alertasPage.totalPaginas()
        );
    }
}
