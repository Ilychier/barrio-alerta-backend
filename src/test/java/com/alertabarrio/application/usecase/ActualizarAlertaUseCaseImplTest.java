package com.alertabarrio.application.usecase;

import com.alertabarrio.application.command.ActualizarAlertaCommand;
import com.alertabarrio.application.dto.AlertaDTO;
import com.alertabarrio.application.mapper.AlertaDomainMapper;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.model.Alerta;
import com.alertabarrio.domain.model.valueobject.AlertaId;
import com.alertabarrio.domain.port.out.AlertaRepositoryPort;
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
@DisplayName("ActualizarAlertaUseCaseImpl")
class ActualizarAlertaUseCaseImplTest {

    @Mock
    private AlertaRepositoryPort repository;

    @Mock
    private AlertaDomainMapper mapper;

    @InjectMocks
    private ActualizarAlertaUseCaseImpl useCase;

    @Test
    @DisplayName("actualiza correctamente y devuelve DTO")
    void execute_conCommandValido_actualizaYDevuelveDTO() {
        ActualizarAlertaCommand command = new ActualizarAlertaCommand(1L, "Robo actualizado", true, 1L, 1L);
        Alerta existente = Alerta.reconstruir(1L, "Robo", true, LocalDateTime.now(), 1L, 1L);
        Alerta actualizada = Alerta.reconstruir(1L, "Robo actualizado", true, existente.getFechaHora(), 1L, 1L);
        AlertaDTO dtoEsperado = new AlertaDTO(1L, "Robo actualizado", true, existente.getFechaHora(), 1L, 1L);

        when(repository.findById(new AlertaId(1L))).thenReturn(Optional.of(existente));
        when(repository.save(any(Alerta.class))).thenReturn(actualizada);
        when(mapper.toDto(actualizada)).thenReturn(dtoEsperado);

        AlertaDTO resultado = useCase.execute(command);

        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        assertEquals("Robo actualizado", resultado.descripcion());
        assertTrue(resultado.esSos());

        verify(repository).findById(new AlertaId(1L));
        verify(repository).save(any(Alerta.class));
        verify(mapper).toDto(actualizada);
    }

    @Test
    @DisplayName("con id inexistente lanza ResourceNotFoundException")
    void execute_conIdInexistente_lanzaResourceNotFoundException() {
        ActualizarAlertaCommand command = new ActualizarAlertaCommand(99L, "Robo", true, 1L, 1L);

        when(repository.findById(new AlertaId(99L))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> useCase.execute(command));

        verify(repository).findById(new AlertaId(99L));
        verify(repository, never()).save(any());
        verify(mapper, never()).toDto(any());
    }
}
