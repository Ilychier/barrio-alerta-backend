package com.alertabarrio.application.usecase;

import com.alertabarrio.application.command.ActualizarEvidenciaCommand;
import com.alertabarrio.application.dto.EvidenciaDTO;
import com.alertabarrio.application.mapper.EvidenciaDomainMapper;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.model.Evidencia;
import com.alertabarrio.domain.model.valueobject.AlertaId;
import com.alertabarrio.domain.model.valueobject.EvidenciaId;
import com.alertabarrio.domain.port.out.AlertaRepositoryPort;
import com.alertabarrio.domain.port.out.EvidenciaRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ActualizarEvidenciaUseCaseImpl")
class ActualizarEvidenciaUseCaseImplTest {

    @Mock
    private EvidenciaRepositoryPort evidenciaRepository;

    @Mock
    private AlertaRepositoryPort alertaRepository;

    @Mock
    private EvidenciaDomainMapper mapper;

    @InjectMocks
    private ActualizarEvidenciaUseCaseImpl useCase;

    @Test
    @DisplayName("actualiza correctamente y devuelve DTO")
    void execute_conCommandValido_actualizaYDevuelveDTO() {
        LocalDateTime fecha = LocalDateTime.of(2024, 1, 1, 0, 0);
        ActualizarEvidenciaCommand command = new ActualizarEvidenciaCommand(1L, "nueva-foto.jpg", 2L);
        Evidencia existente = Evidencia.reconstruir(1L, "foto.jpg", fecha, 1L);
        Evidencia actualizada = Evidencia.reconstruir(1L, "nueva-foto.jpg", fecha, 2L);
        EvidenciaDTO dtoEsperado = new EvidenciaDTO(1L, "nueva-foto.jpg", fecha, 2L);

        when(evidenciaRepository.findById(new EvidenciaId(1L))).thenReturn(Optional.of(existente));
        when(alertaRepository.existsById(new AlertaId(2L))).thenReturn(true);
        when(evidenciaRepository.save(any(Evidencia.class))).thenReturn(actualizada);
        when(mapper.toDto(actualizada)).thenReturn(dtoEsperado);

        EvidenciaDTO resultado = useCase.execute(command);

        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        assertEquals("nueva-foto.jpg", resultado.archivoUrl());
        assertEquals(2L, resultado.alertaId());

        verify(evidenciaRepository).findById(new EvidenciaId(1L));
        verify(alertaRepository).existsById(new AlertaId(2L));
        verify(evidenciaRepository).save(any(Evidencia.class));
        verify(mapper).toDto(actualizada);
    }

    @Test
    @DisplayName("con id inexistente lanza ResourceNotFoundException")
    void execute_conIdInexistente_lanzaExcepcion() {
        ActualizarEvidenciaCommand command = new ActualizarEvidenciaCommand(99L, "foto.jpg", 1L);

        when(evidenciaRepository.findById(new EvidenciaId(99L))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> useCase.execute(command));

        verify(evidenciaRepository).findById(new EvidenciaId(99L));
        verify(alertaRepository, never()).existsById(any());
        verify(evidenciaRepository, never()).save(any());
    }
}
