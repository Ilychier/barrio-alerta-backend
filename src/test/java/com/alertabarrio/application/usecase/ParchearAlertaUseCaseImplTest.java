package com.alertabarrio.application.usecase;

import com.alertabarrio.application.command.ParchearAlertaCommand;
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
@DisplayName("ParchearAlertaUseCaseImpl")
class ParchearAlertaUseCaseImplTest {

    @Mock
    private AlertaRepositoryPort repository;

    @Mock
    private AlertaDomainMapper mapper;

    @InjectMocks
    private ParchearAlertaUseCaseImpl useCase;

    @Test
    @DisplayName("parchea solo la descripción manteniendo el resto")
    void execute_conDescripcionNueva_parcheaSoloDescripcion() {
        ParchearAlertaCommand command = new ParchearAlertaCommand(1L, "Nueva desc", null, null, null);
        Alerta existente = Alerta.reconstruir(1L, "Original", true, LocalDateTime.now(), 1L, 1L);
        Alerta parcheada = Alerta.reconstruir(1L, "Nueva desc", true, existente.getFechaHora(), 1L, 1L);
        AlertaDTO dtoEsperado = new AlertaDTO(1L, "Nueva desc", true, existente.getFechaHora(), 1L, 1L);

        when(repository.findById(new AlertaId(1L))).thenReturn(Optional.of(existente));
        when(repository.save(any(Alerta.class))).thenReturn(parcheada);
        when(mapper.toDto(parcheada)).thenReturn(dtoEsperado);

        AlertaDTO resultado = useCase.execute(command);

        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        assertEquals("Nueva desc", resultado.descripcion());
        assertTrue(resultado.esSos());
        assertEquals(1L, resultado.categoriaId());

        verify(repository).findById(new AlertaId(1L));
        verify(repository).save(any(Alerta.class));
        verify(mapper).toDto(parcheada);
    }

    @Test
    @DisplayName("con id inexistente lanza ResourceNotFoundException")
    void execute_conIdInexistente_lanzaResourceNotFoundException() {
        ParchearAlertaCommand command = new ParchearAlertaCommand(99L, "Nueva desc", null, null, null);

        when(repository.findById(new AlertaId(99L))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> useCase.execute(command));

        verify(repository).findById(new AlertaId(99L));
        verify(repository, never()).save(any());
        verify(mapper, never()).toDto(any());
    }
}
