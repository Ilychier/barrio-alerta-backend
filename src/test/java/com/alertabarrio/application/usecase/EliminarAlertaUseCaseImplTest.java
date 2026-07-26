package com.alertabarrio.application.usecase;

import com.alertabarrio.application.command.EliminarAlertaCommand;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.model.valueobject.AlertaId;
import com.alertabarrio.domain.port.out.AlertaRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EliminarAlertaUseCaseImpl")
class EliminarAlertaUseCaseImplTest {

    @Mock
    private AlertaRepositoryPort repository;

    @InjectMocks
    private EliminarAlertaUseCaseImpl useCase;

    @Test
    @DisplayName("elimina alerta existente")
    void execute_conIdValido_eliminaAlerta() {
        EliminarAlertaCommand command = new EliminarAlertaCommand(1L);

        when(repository.existsById(new AlertaId(1L))).thenReturn(true);

        useCase.execute(command);

        verify(repository).existsById(new AlertaId(1L));
        verify(repository).deleteById(new AlertaId(1L));
    }

    @Test
    @DisplayName("con id inexistente lanza ResourceNotFoundException")
    void execute_conIdInexistente_lanzaResourceNotFoundException() {
        EliminarAlertaCommand command = new EliminarAlertaCommand(99L);

        when(repository.existsById(new AlertaId(99L))).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> useCase.execute(command));

        verify(repository).existsById(new AlertaId(99L));
        verify(repository, never()).deleteById(any());
    }
}
