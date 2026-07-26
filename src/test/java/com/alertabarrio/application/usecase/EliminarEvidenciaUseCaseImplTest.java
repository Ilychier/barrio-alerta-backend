package com.alertabarrio.application.usecase;

import com.alertabarrio.application.command.EliminarEvidenciaCommand;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.model.valueobject.EvidenciaId;
import com.alertabarrio.domain.port.out.EvidenciaRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EliminarEvidenciaUseCaseImpl")
class EliminarEvidenciaUseCaseImplTest {

    @Mock
    private EvidenciaRepositoryPort evidenciaRepository;

    @InjectMocks
    private EliminarEvidenciaUseCaseImpl useCase;

    @Test
    @DisplayName("elimina evidencia existente")
    void execute_conIdExistente_eliminaCorrectamente() {
        EliminarEvidenciaCommand command = new EliminarEvidenciaCommand(1L);

        when(evidenciaRepository.existsById(new EvidenciaId(1L))).thenReturn(true);

        useCase.execute(command);

        verify(evidenciaRepository).existsById(new EvidenciaId(1L));
        verify(evidenciaRepository).deleteById(new EvidenciaId(1L));
    }

    @Test
    @DisplayName("con id inexistente lanza ResourceNotFoundException")
    void execute_conIdInexistente_lanzaExcepcion() {
        EliminarEvidenciaCommand command = new EliminarEvidenciaCommand(99L);

        when(evidenciaRepository.existsById(new EvidenciaId(99L))).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> useCase.execute(command));

        verify(evidenciaRepository).existsById(new EvidenciaId(99L));
        verify(evidenciaRepository, never()).deleteById(any());
    }
}
