package com.alertabarrio.application.usecase;

import com.alertabarrio.application.command.LoginCommand;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.model.User;
import com.alertabarrio.domain.port.out.PasswordEncoderPort;
import com.alertabarrio.domain.port.out.TokenServicePort;
import com.alertabarrio.domain.port.out.UsuarioRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("LoginUseCaseImpl")
class LoginUseCaseImplTest {

    @Mock
    private UsuarioRepositoryPort usuarioRepository;

    @Mock
    private PasswordEncoderPort passwordEncoder;

    @Mock
    private TokenServicePort tokenService;

    @InjectMocks
    private LoginUseCaseImpl useCase;

    @Test
    @DisplayName("con credenciales válidas devuelve token")
    void execute_conCredencialesValidas_devuelveToken() {
        LoginCommand command = new LoginCommand("juan@test.com", "password123");
        User user = User.reconstruir(1L, "Juan", "juan@test.com", "3001234567", "Calle 1", "hashed123", 1L);

        when(usuarioRepository.findByEmail("juan@test.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.verificar("password123", "hashed123")).thenReturn(true);
        when(tokenService.generarToken(user.getEmail())).thenReturn("jwt-token-123");

        String resultado = useCase.execute(command);

        assertNotNull(resultado);
        assertEquals("jwt-token-123", resultado);

        verify(usuarioRepository).findByEmail("juan@test.com");
        verify(passwordEncoder).verificar("password123", "hashed123");
        verify(tokenService).generarToken(user.getEmail());
    }

    @Test
    @DisplayName("con email inexistente lanza ResourceNotFoundException")
    void execute_conEmailInexistente_lanzaExcepcion() {
        LoginCommand command = new LoginCommand("noexiste@test.com", "password123");

        when(usuarioRepository.findByEmail("noexiste@test.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> useCase.execute(command));

        verify(usuarioRepository).findByEmail("noexiste@test.com");
        verify(passwordEncoder, never()).verificar(any(), any());
        verify(tokenService, never()).generarToken(any());
    }

    @Test
    @DisplayName("con password incorrecto lanza IllegalArgumentException")
    void execute_conPasswordIncorrecto_lanzaExcepcion() {
        LoginCommand command = new LoginCommand("juan@test.com", "wrongpassword");
        User user = User.reconstruir(1L, "Juan", "juan@test.com", "3001234567", "Calle 1", "hashed123", 1L);

        when(usuarioRepository.findByEmail("juan@test.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.verificar("wrongpassword", "hashed123")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(command));

        verify(usuarioRepository).findByEmail("juan@test.com");
        verify(passwordEncoder).verificar("wrongpassword", "hashed123");
        verify(tokenService, never()).generarToken(any());
    }
}
