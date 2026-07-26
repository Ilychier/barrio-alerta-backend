package com.alertabarrio.application.usecase;

import com.alertabarrio.application.command.CrearEvidenciaCommand;
import com.alertabarrio.application.dto.EvidenciaDTO;
import com.alertabarrio.application.mapper.EvidenciaDomainMapper;
import com.alertabarrio.domain.exception.EvidenciaInvalidaException;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.model.Evidencia;
import com.alertabarrio.domain.model.valueobject.AlertaId;
import com.alertabarrio.domain.port.out.AlertaRepositoryPort;
import com.alertabarrio.domain.port.out.EvidenciaRepositoryPort;
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
@DisplayName("CrearEvidenciaUseCaseImpl")
class CrearEvidenciaUseCaseImplTest {

    @Mock
    private EvidenciaRepositoryPort evidenciaRepository;

    @Mock
    private AlertaRepositoryPort alertaRepository;

    @Mock
    private EvidenciaDomainMapper mapper;

    @Mock
    private Clock clock;

    @InjectMocks
    private CrearEvidenciaUseCaseImpl useCase;

    @Test
    @DisplayName("con command válido persiste y devuelve DTO")
    void execute_conCommandValido_persisteYDevuelveDTO() {
        Clock fixedClock = Clock.fixed(Instant.parse("2024-01-01T00:00:00Z"), ZoneId.of("America/Bogota"));
        when(clock.instant()).thenReturn(fixedClock.instant());
        when(clock.getZone()).thenReturn(fixedClock.getZone());

        CrearEvidenciaCommand command = new CrearEvidenciaCommand("foto.jpg", 1L);
        LocalDateTime now = LocalDateTime.now(fixedClock);
        Evidencia evidenciaGuardada = Evidencia.reconstruir(1L, "foto.jpg", now, 1L);
        EvidenciaDTO dtoEsperado = new EvidenciaDTO(1L, "foto.jpg", now, 1L);

        when(alertaRepository.existsById(new AlertaId(1L))).thenReturn(true);
        when(evidenciaRepository.save(any(Evidencia.class))).thenReturn(evidenciaGuardada);
        when(mapper.toDto(evidenciaGuardada)).thenReturn(dtoEsperado);

        EvidenciaDTO resultado = useCase.execute(command);

        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        assertEquals("foto.jpg", resultado.archivoUrl());
        assertEquals(now, resultado.fechaSubida());
        assertEquals(1L, resultado.alertaId());

        verify(alertaRepository).existsById(new AlertaId(1L));
        verify(evidenciaRepository).save(any(Evidencia.class));
        verify(mapper).toDto(evidenciaGuardada);
    }

    @Test
    @DisplayName("con archivoUrl inválido lanza EvidenciaInvalidaException")
    void execute_conArchivoUrlInvalido_lanzaExcepcion() {
        CrearEvidenciaCommand command = new CrearEvidenciaCommand("", 1L);

        when(alertaRepository.existsById(new AlertaId(1L))).thenReturn(true);

        assertThrows(EvidenciaInvalidaException.class, () -> useCase.execute(command));

        verify(alertaRepository).existsById(new AlertaId(1L));
        verify(evidenciaRepository, never()).save(any());
    }

    @Test
    @DisplayName("con alerta inexistente lanza ResourceNotFoundException")
    void execute_conAlertaInexistente_lanzaExcepcion() {
        CrearEvidenciaCommand command = new CrearEvidenciaCommand("foto.jpg", 99L);

        when(alertaRepository.existsById(new AlertaId(99L))).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> useCase.execute(command));

        verify(alertaRepository).existsById(new AlertaId(99L));
        verify(evidenciaRepository, never()).save(any());
    }
}
