package com.alertabarrio.application.usecase;

import com.alertabarrio.application.command.RegistrarUsuarioCommand;
import com.alertabarrio.application.dto.UsuarioDTO;
import com.alertabarrio.application.mapper.UsuarioDomainMapper;
import com.alertabarrio.domain.exception.ResourceConflictException;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.model.User;
import com.alertabarrio.domain.model.valueobject.BarrioId;
import com.alertabarrio.domain.port.out.BarrioRepositoryPort;
import com.alertabarrio.domain.port.out.PasswordEncoderPort;
import com.alertabarrio.domain.port.out.UsuarioRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RegistrarUsuarioUseCaseImpl")
class RegistrarUsuarioUseCaseImplTest {

    @Mock
    private UsuarioRepositoryPort usuarioRepository;

    @Mock
    private PasswordEncoderPort passwordEncoder;

    @Mock
    private BarrioRepositoryPort barrioRepository;

    @Mock
    private UsuarioDomainMapper mapper;

    @InjectMocks
    private RegistrarUsuarioUseCaseImpl useCase;

    @Test
    @DisplayName("con command válido registra y devuelve DTO")
    void execute_conCommandValido_registraYDevuelveDTO() {
        RegistrarUsuarioCommand command = new RegistrarUsuarioCommand("Juan", "juan@test.com", "3001234567", "Calle 1", "password123", 1L);
        User userGuardado = User.reconstruir(1L, "Juan", "juan@test.com", "3001234567", "Calle 1", "hashed123", 1L);
        UsuarioDTO dtoEsperado = new UsuarioDTO(1L, "Juan", "juan@test.com", "3001234567", "Calle 1", 1L);

        when(usuarioRepository.existsByEmail("juan@test.com")).thenReturn(false);
        when(barrioRepository.existsById(new BarrioId(1L))).thenReturn(true);
        when(passwordEncoder.hashear("password123")).thenReturn("hashed123");
        when(usuarioRepository.save(any(User.class))).thenReturn(userGuardado);
        when(mapper.toDto(userGuardado)).thenReturn(dtoEsperado);

        UsuarioDTO resultado = useCase.execute(command);

        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        assertEquals("Juan", resultado.name());
        assertEquals("juan@test.com", resultado.email());
        assertEquals("3001234567", resultado.phone());
        assertEquals("Calle 1", resultado.address());
        assertEquals(1L, resultado.barrioId());

        verify(usuarioRepository).existsByEmail("juan@test.com");
        verify(barrioRepository).existsById(new BarrioId(1L));
        verify(passwordEncoder).hashear("password123");
        verify(usuarioRepository).save(any(User.class));
        verify(mapper).toDto(userGuardado);
    }

    @Test
    @DisplayName("con email duplicado lanza ResourceConflictException")
    void execute_conEmailDuplicado_lanzaResourceConflictException() {
        RegistrarUsuarioCommand command = new RegistrarUsuarioCommand("Juan", "juan@test.com", "3001234567", "Calle 1", "password123", 1L);

        when(usuarioRepository.existsByEmail("juan@test.com")).thenReturn(true);

        assertThrows(ResourceConflictException.class, () -> useCase.execute(command));

        verify(usuarioRepository).existsByEmail("juan@test.com");
        verifyNoInteractions(passwordEncoder, barrioRepository, mapper);
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("con barrio inexistente lanza ResourceNotFoundException")
    void execute_conBarrioInexistente_lanzaResourceNotFoundException() {
        RegistrarUsuarioCommand command = new RegistrarUsuarioCommand("Juan", "juan@test.com", "3001234567", "Calle 1", "password123", 99L);

        when(usuarioRepository.existsByEmail("juan@test.com")).thenReturn(false);
        when(barrioRepository.existsById(new BarrioId(99L))).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> useCase.execute(command));

        verify(usuarioRepository).existsByEmail("juan@test.com");
        verify(barrioRepository).existsById(new BarrioId(99L));
        verifyNoInteractions(passwordEncoder, mapper);
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("sin barrioId registra sin barrio")
    void execute_sinBarrioId_registraSinBarrio() {
        RegistrarUsuarioCommand command = new RegistrarUsuarioCommand("Juan", "juan@test.com", "3001234567", "Calle 1", "password123", null);
        User userGuardado = User.reconstruir(1L, "Juan", "juan@test.com", "3001234567", "Calle 1", "hashed123", null);
        UsuarioDTO dtoEsperado = new UsuarioDTO(1L, "Juan", "juan@test.com", "3001234567", "Calle 1", null);

        when(usuarioRepository.existsByEmail("juan@test.com")).thenReturn(false);
        when(passwordEncoder.hashear("password123")).thenReturn("hashed123");
        when(usuarioRepository.save(any(User.class))).thenReturn(userGuardado);
        when(mapper.toDto(userGuardado)).thenReturn(dtoEsperado);

        UsuarioDTO resultado = useCase.execute(command);

        assertNotNull(resultado);
        assertNull(resultado.barrioId());

        verify(usuarioRepository).existsByEmail("juan@test.com");
        verify(passwordEncoder).hashear("password123");
        verify(usuarioRepository).save(any(User.class));
        verify(mapper).toDto(userGuardado);
        verifyNoInteractions(barrioRepository);
    }
}
