package com.alertabarrio.application.usecase;

import com.alertabarrio.application.command.ActualizarUsuarioCommand;
import com.alertabarrio.application.dto.UsuarioDTO;
import com.alertabarrio.application.mapper.UsuarioDomainMapper;
import com.alertabarrio.domain.exception.ResourceConflictException;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.model.User;
import com.alertabarrio.domain.model.valueobject.BarrioId;
import com.alertabarrio.domain.model.valueobject.UsuarioId;
import com.alertabarrio.domain.port.out.BarrioRepositoryPort;
import com.alertabarrio.domain.port.out.PasswordEncoderPort;
import com.alertabarrio.domain.port.out.UsuarioRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ActualizarUsuarioUseCaseImpl")
class ActualizarUsuarioUseCaseImplTest {

    @Mock
    private UsuarioRepositoryPort usuarioRepository;

    @Mock
    private PasswordEncoderPort passwordEncoder;

    @Mock
    private BarrioRepositoryPort barrioRepository;

    @Mock
    private UsuarioDomainMapper mapper;

    @InjectMocks
    private ActualizarUsuarioUseCaseImpl useCase;

    @Test
    @DisplayName("actualiza correctamente con todos los campos")
    void execute_conCommandValido_actualizaYDevuelveDTO() {
        ActualizarUsuarioCommand command = new ActualizarUsuarioCommand(1L, "Juan Updated", "juan.new@test.com", "3007654321", "Calle 2", "newpass", 2L);
        User existente = User.reconstruir(1L, "Juan", "juan@test.com", "3001234567", "Calle 1", "oldhash", 1L);
        User actualizado = User.reconstruir(1L, "Juan Updated", "juan.new@test.com", "3007654321", "Calle 2", "newhash", 2L);
        UsuarioDTO dtoEsperado = new UsuarioDTO(1L, "Juan Updated", "juan.new@test.com", "3007654321", "Calle 2", 2L);

        when(usuarioRepository.findById(new UsuarioId(1L))).thenReturn(Optional.of(existente));
        when(usuarioRepository.existsByEmail("juan.new@test.com")).thenReturn(false);
        when(barrioRepository.existsById(new BarrioId(2L))).thenReturn(true);
        when(passwordEncoder.hashear("newpass")).thenReturn("newhash");
        when(usuarioRepository.save(any(User.class))).thenReturn(actualizado);
        when(mapper.toDto(actualizado)).thenReturn(dtoEsperado);

        UsuarioDTO resultado = useCase.execute(command);

        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        assertEquals("Juan Updated", resultado.name());
        assertEquals("juan.new@test.com", resultado.email());
        assertEquals("3007654321", resultado.phone());
        assertEquals("Calle 2", resultado.address());
        assertEquals(2L, resultado.barrioId());

        verify(usuarioRepository).findById(new UsuarioId(1L));
        verify(usuarioRepository).existsByEmail("juan.new@test.com");
        verify(barrioRepository).existsById(new BarrioId(2L));
        verify(passwordEncoder).hashear("newpass");
        verify(usuarioRepository).save(any(User.class));
        verify(mapper).toDto(actualizado);
    }

    @Test
    @DisplayName("con id inexistente lanza ResourceNotFoundException")
    void execute_conIdInexistente_lanzaResourceNotFoundException() {
        ActualizarUsuarioCommand command = new ActualizarUsuarioCommand(99L, "Juan", "juan@test.com", "3001234567", "Calle 1", "password123", 1L);

        when(usuarioRepository.findById(new UsuarioId(99L))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> useCase.execute(command));

        verify(usuarioRepository).findById(new UsuarioId(99L));
        verifyNoMoreInteractions(usuarioRepository);
        verifyNoInteractions(passwordEncoder, barrioRepository, mapper);
    }

    @Test
    @DisplayName("con email duplicado de otro usuario lanza ResourceConflictException")
    void execute_conEmailDuplicado_lanzaResourceConflictException() {
        ActualizarUsuarioCommand command = new ActualizarUsuarioCommand(1L, "Juan", "existente@test.com", "3001234567", "Calle 1", "password123", 1L);
        User existente = User.reconstruir(1L, "Juan", "juan@test.com", "3001234567", "Calle 1", "hash", 1L);

        when(usuarioRepository.findById(new UsuarioId(1L))).thenReturn(Optional.of(existente));
        when(usuarioRepository.existsByEmail("existente@test.com")).thenReturn(true);

        assertThrows(ResourceConflictException.class, () -> useCase.execute(command));

        verify(usuarioRepository).findById(new UsuarioId(1L));
        verify(usuarioRepository).existsByEmail("existente@test.com");
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("con barrio inexistente lanza ResourceNotFoundException")
    void execute_conBarrioInexistente_lanzaResourceNotFoundException() {
        ActualizarUsuarioCommand command = new ActualizarUsuarioCommand(1L, "Juan", "juan@test.com", "3001234567", "Calle 1", "password123", 99L);
        User existente = User.reconstruir(1L, "Juan", "juan@test.com", "3001234567", "Calle 1", "hash", 1L);

        when(usuarioRepository.findById(new UsuarioId(1L))).thenReturn(Optional.of(existente));
        when(barrioRepository.existsById(new BarrioId(99L))).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> useCase.execute(command));

        verify(usuarioRepository).findById(new UsuarioId(1L));
        verify(barrioRepository).existsById(new BarrioId(99L));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("sin password ni barrioId conserva valores existentes")
    void execute_sinPasswordNiBarrio_conservaValores() {
        ActualizarUsuarioCommand command = new ActualizarUsuarioCommand(1L, "Juan Updated", "juan@test.com", "3001234567", "Calle 2", null, null);
        User existente = User.reconstruir(1L, "Juan", "juan@test.com", "3001234567", "Calle 1", "oldhash", 1L);
        User actualizado = User.reconstruir(1L, "Juan Updated", "juan@test.com", "3001234567", "Calle 2", "oldhash", null);
        UsuarioDTO dtoEsperado = new UsuarioDTO(1L, "Juan Updated", "juan@test.com", "3001234567", "Calle 2", null);

        when(usuarioRepository.findById(new UsuarioId(1L))).thenReturn(Optional.of(existente));
        when(usuarioRepository.save(any(User.class))).thenReturn(actualizado);
        when(mapper.toDto(actualizado)).thenReturn(dtoEsperado);

        UsuarioDTO resultado = useCase.execute(command);

        assertNotNull(resultado);
        assertEquals("Juan Updated", resultado.name());
        assertNull(resultado.barrioId());

        verify(usuarioRepository).findById(new UsuarioId(1L));
        verify(passwordEncoder, never()).hashear(any());
        verify(usuarioRepository).save(any(User.class));
        verify(mapper).toDto(actualizado);
    }
}
