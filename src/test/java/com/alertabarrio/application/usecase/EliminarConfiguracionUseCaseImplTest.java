package com.alertabarrio.application.usecase;

import com.alertabarrio.application.command.EliminarConfiguracionCommand;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.model.valueobject.ConfiguracionId;
import com.alertabarrio.domain.port.out.ConfiguracionRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EliminarConfiguracionUseCaseImpl")
class EliminarConfiguracionUseCaseImplTest {

    @Mock
    private ConfiguracionRepositoryPort configuracionRepository;

    @InjectMocks
    private EliminarConfiguracionUseCaseImpl useCase;

    @Test
    @DisplayName("elimina configuración existente")
    void execute_conIdExistente_elimina() {
        EliminarConfiguracionCommand command = new EliminarConfiguracionCommand(1L);
        ConfiguracionId id = new ConfiguracionId(1L);

        when(configuracionRepository.existsById(id)).thenReturn(true);

        useCase.execute(command);

        verify(configuracionRepository).existsById(id);
        verify(configuracionRepository).deleteById(id);
    }

    @Test
    @DisplayName("con id inexistente lanza ResourceNotFoundException")
    void execute_conIdInexistente_lanzaExcepcion() {
        EliminarConfiguracionCommand command = new EliminarConfiguracionCommand(99L);
        ConfiguracionId id = new ConfiguracionId(99L);

        when(configuracionRepository.existsById(id)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> useCase.execute(command));

        verify(configuracionRepository).existsById(id);
        verify(configuracionRepository, never()).deleteById(any());
    }
}
