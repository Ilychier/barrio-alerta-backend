package com.alertabarrio.application.usecase;

import com.alertabarrio.application.command.CrearAlertaCommand;
import com.alertabarrio.application.dto.AlertaDTO;
import com.alertabarrio.application.mapper.AlertaDomainMapper;
import com.alertabarrio.domain.exception.AlertaInvalidaException;
import com.alertabarrio.domain.model.Alerta;
import com.alertabarrio.domain.port.out.AlertaRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CrearAlertaUseCaseImpl")
class CrearAlertaUseCaseImplTest {

    @Mock
    private AlertaRepositoryPort repository;

    @Mock
    private AlertaDomainMapper mapper;

    @Mock
    private Clock clock;

    @InjectMocks
    private CrearAlertaUseCaseImpl useCase;

    @Test
    @DisplayName("con command válido persiste y devuelve DTO")
    void execute_conCommandValido_persisteYDevuelveDTO() {
        Clock fixedClock = Clock.fixed(Instant.parse("2024-01-01T00:00:00Z"), ZoneId.of("America/Bogota"));
        when(clock.instant()).thenReturn(fixedClock.instant());
        when(clock.getZone()).thenReturn(fixedClock.getZone());

        CrearAlertaCommand command = new CrearAlertaCommand("Robo en casa", true, 1L, 1L);
        LocalDateTime expectedFechaHora = LocalDateTime.now(fixedClock);
        Alerta alertaPersistida = Alerta.reconstruir(1L, "Robo en casa", true, expectedFechaHora, 1L, 1L);
        AlertaDTO dtoEsperado = new AlertaDTO(1L, "Robo en casa", true, expectedFechaHora, 1L, 1L);

        when(repository.save(any(Alerta.class))).thenReturn(alertaPersistida);
        when(mapper.toDto(alertaPersistida)).thenReturn(dtoEsperado);

        AlertaDTO resultado = useCase.execute(command);

        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        assertEquals("Robo en casa", resultado.descripcion());
        assertTrue(resultado.esSos());
        assertEquals(expectedFechaHora, resultado.fechaHora());
        assertEquals(1L, resultado.usuarioId());
        assertEquals(1L, resultado.categoriaId());

        verify(repository).save(any(Alerta.class));
        verify(mapper).toDto(alertaPersistida);
    }

    @Test
    @DisplayName("con descripción inválida lanza AlertaInvalidaException")
    void execute_conDescripcionInvalida_lanzaExcepcion() {
        CrearAlertaCommand command = new CrearAlertaCommand(null, true, 1L, 1L);

        assertThrows(AlertaInvalidaException.class, () -> useCase.execute(command));

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("sin categoriaId crea alerta sin categoría")
    void execute_sinCategoriaId_creaAlertaSinCategoria() {
        Clock fixedClock = Clock.fixed(Instant.parse("2024-01-01T00:00:00Z"), ZoneId.of("America/Bogota"));
        when(clock.instant()).thenReturn(fixedClock.instant());
        when(clock.getZone()).thenReturn(fixedClock.getZone());

        CrearAlertaCommand command = new CrearAlertaCommand("Robo", true, 1L, null);
        LocalDateTime expectedFechaHora = LocalDateTime.now(fixedClock);
        Alerta alertaPersistida = Alerta.reconstruir(1L, "Robo", true, expectedFechaHora, 1L, null);
        AlertaDTO dtoEsperado = new AlertaDTO(1L, "Robo", true, expectedFechaHora, 1L, null);

        when(repository.save(any(Alerta.class))).thenReturn(alertaPersistida);
        when(mapper.toDto(alertaPersistida)).thenReturn(dtoEsperado);

        AlertaDTO resultado = useCase.execute(command);

        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        assertEquals("Robo", resultado.descripcion());
        assertNull(resultado.categoriaId());

        verify(repository).save(any(Alerta.class));
        verify(mapper).toDto(alertaPersistida);
    }
}
