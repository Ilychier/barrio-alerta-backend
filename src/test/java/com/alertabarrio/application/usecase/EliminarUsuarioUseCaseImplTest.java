package com.alertabarrio.application.usecase;

import com.alertabarrio.application.command.EliminarUsuarioCommand;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.model.valueobject.UsuarioId;
import com.alertabarrio.domain.port.out.UsuarioRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EliminarUsuarioUseCaseImpl")
class EliminarUsuarioUseCaseImplTest {

    @Mock
    private UsuarioRepositoryPort usuarioRepository;

    @InjectMocks
    private EliminarUsuarioUseCaseImpl useCase;

    @Test
    @DisplayName("elimina usuario existente")
    void execute_conIdValido_eliminaUsuario() {
        EliminarUsuarioCommand command = new EliminarUsuarioCommand(1L);

        when(usuarioRepository.existsById(new UsuarioId(1L))).thenReturn(true);

        useCase.execute(command);

        verify(usuarioRepository).existsById(new UsuarioId(1L));
        verify(usuarioRepository).deleteById(new UsuarioId(1L));
    }

    @Test
    @DisplayName("con id inexistente lanza ResourceNotFoundException")
    void execute_conIdInexistente_lanzaResourceNotFoundException() {
        EliminarUsuarioCommand command = new EliminarUsuarioCommand(99L);

        when(usuarioRepository.existsById(new UsuarioId(99L))).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> useCase.execute(command));

        verify(usuarioRepository).existsById(new UsuarioId(99L));
        verify(usuarioRepository, never()).deleteById(any());
    }
}
