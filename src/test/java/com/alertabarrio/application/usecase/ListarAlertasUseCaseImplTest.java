package com.alertabarrio.application.usecase;

import com.alertabarrio.application.dto.AlertaDTO;
import com.alertabarrio.application.mapper.AlertaDomainMapper;
import com.alertabarrio.application.query.ListarAlertasQuery;
import com.alertabarrio.domain.model.Alerta;
import com.alertabarrio.domain.port.out.AlertaRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ListarAlertasUseCaseImpl")
class ListarAlertasUseCaseImplTest {

    @Mock
    private AlertaRepositoryPort repository;

    @Mock
    private AlertaDomainMapper mapper;

    @InjectMocks
    private ListarAlertasUseCaseImpl useCase;

    @Test
    @DisplayName("sin filtros devuelve página completa")
    void execute_sinFiltros_devuelvePagina() {
        Pageable pageable = Pageable.ofSize(10);
        ListarAlertasQuery query = new ListarAlertasQuery(pageable, null, null);
        Alerta alerta = Alerta.reconstruir(1L, "Robo", true, LocalDateTime.now(), 1L, 1L);
        Page<Alerta> pagina = new PageImpl<>(List.of(alerta));
        AlertaDTO dto = new AlertaDTO(1L, "Robo", true, alerta.getFechaHora(), 1L, 1L);

        when(repository.findAll(pageable)).thenReturn(pagina);
        when(mapper.toDto(alerta)).thenReturn(dto);

        Page<AlertaDTO> resultado = useCase.execute(query);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals("Robo", resultado.getContent().get(0).descripcion());

        verify(repository).findAll(pageable);
        verify(repository, never()).findByFechaHoraBetween(any(), any(), any());
        verify(repository, never()).findByUsuario_Barrio_IdAndFechaHoraBetween(any(), any(), any(), any());
    }

    @Test
    @DisplayName("con fecha filtra por fecha")
    void execute_conFecha_filtraPorFecha() {
        Pageable pageable = Pageable.ofSize(10);
        LocalDate fecha = LocalDate.of(2024, 1, 1);
        ListarAlertasQuery query = new ListarAlertasQuery(pageable, fecha, null);
        Alerta alerta = Alerta.reconstruir(1L, "Robo", true, LocalDateTime.now(), 1L, 1L);
        Page<Alerta> pagina = new PageImpl<>(List.of(alerta));
        AlertaDTO dto = new AlertaDTO(1L, "Robo", true, alerta.getFechaHora(), 1L, 1L);

        when(repository.findByFechaHoraBetween(any(LocalDateTime.class), any(LocalDateTime.class), eq(pageable)))
                .thenReturn(pagina);
        when(mapper.toDto(alerta)).thenReturn(dto);

        Page<AlertaDTO> resultado = useCase.execute(query);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());

        verify(repository).findByFechaHoraBetween(any(LocalDateTime.class), any(LocalDateTime.class), eq(pageable));
        verify(repository, never()).findAll(any());
        verify(repository, never()).findByUsuario_Barrio_IdAndFechaHoraBetween(any(), any(), any(), any());
    }

    @Test
    @DisplayName("con fecha y barrioId filtra por ambos")
    void execute_conFechaYBarrioId_filtraPorAmbos() {
        Pageable pageable = Pageable.ofSize(10);
        LocalDate fecha = LocalDate.of(2024, 1, 1);
        ListarAlertasQuery query = new ListarAlertasQuery(pageable, fecha, 1L);
        Alerta alerta = Alerta.reconstruir(1L, "Robo", true, LocalDateTime.now(), 1L, 1L);
        Page<Alerta> pagina = new PageImpl<>(List.of(alerta));
        AlertaDTO dto = new AlertaDTO(1L, "Robo", true, alerta.getFechaHora(), 1L, 1L);

        when(repository.findByUsuario_Barrio_IdAndFechaHoraBetween(
                eq(1L), any(LocalDateTime.class), any(LocalDateTime.class), eq(pageable)))
                .thenReturn(pagina);
        when(mapper.toDto(alerta)).thenReturn(dto);

        Page<AlertaDTO> resultado = useCase.execute(query);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());

        verify(repository).findByUsuario_Barrio_IdAndFechaHoraBetween(
                eq(1L), any(LocalDateTime.class), any(LocalDateTime.class), eq(pageable));
        verify(repository, never()).findAll(any());
        verify(repository, never()).findByFechaHoraBetween(any(), any(), any());
    }
}
