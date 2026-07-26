package com.alertabarrio.application.usecase;

import com.alertabarrio.application.command.EliminarBarrioCommand;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.model.valueobject.BarrioId;
import com.alertabarrio.domain.port.out.BarrioRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EliminarBarrioUseCaseImpl")
class EliminarBarrioUseCaseImplTest {

    @Mock
    private BarrioRepositoryPort barrioRepository;

    @InjectMocks
    private EliminarBarrioUseCaseImpl useCase;

    @Test
    @DisplayName("con id existente elimina")
    void execute_conIdExistente_elimina() {
        EliminarBarrioCommand command = new EliminarBarrioCommand(1L);

        when(barrioRepository.existsById(new BarrioId(1L))).thenReturn(true);

        useCase.execute(command);

        verify(barrioRepository).deleteById(new BarrioId(1L));
    }

    @Test
    @DisplayName("con id inexistente lanza ResourceNotFoundException")
    void execute_conIdInexistente_lanzaResourceNotFoundException() {
        EliminarBarrioCommand command = new EliminarBarrioCommand(99L);

        when(barrioRepository.existsById(new BarrioId(99L))).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> useCase.execute(command));

        verify(barrioRepository, never()).deleteById(any());
    }
}
