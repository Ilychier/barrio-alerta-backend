package com.alertabarrio.application.usecase;

import com.alertabarrio.application.command.ParchearUsuarioCommand;
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
@DisplayName("ParchearUsuarioUseCaseImpl")
class ParchearUsuarioUseCaseImplTest {

    @Mock
    private UsuarioRepositoryPort usuarioRepository;

    @Mock
    private PasswordEncoderPort passwordEncoder;

    @Mock
    private BarrioRepositoryPort barrioRepository;

    @Mock
    private UsuarioDomainMapper mapper;

    @InjectMocks
    private ParchearUsuarioUseCaseImpl useCase;

    @Test
    @DisplayName("parchea solo nombre y conserva el resto")
    void execute_parcheaSoloNombre_conservaResto() {
        ParchearUsuarioCommand command = new ParchearUsuarioCommand(1L, "Juan Parcheado", null, null, null, null, null);
        User existente = User.reconstruir(1L, "Juan", "juan@test.com", "3001234567", "Calle 1", "hash", 1L);
        User parcheado = User.reconstruir(1L, "Juan Parcheado", "juan@test.com", "3001234567", "Calle 1", "hash", 1L);
        UsuarioDTO dtoEsperado = new UsuarioDTO(1L, "Juan Parcheado", "juan@test.com", "3001234567", "Calle 1", 1L);

        when(usuarioRepository.findById(new UsuarioId(1L))).thenReturn(Optional.of(existente));
        when(usuarioRepository.save(any(User.class))).thenReturn(parcheado);
        when(mapper.toDto(parcheado)).thenReturn(dtoEsperado);

        UsuarioDTO resultado = useCase.execute(command);

        assertNotNull(resultado);
        assertEquals("Juan Parcheado", resultado.name());
        assertEquals("juan@test.com", resultado.email());
        assertEquals("3001234567", resultado.phone());
        assertEquals("Calle 1", resultado.address());
        assertEquals(1L, resultado.barrioId());

        verify(usuarioRepository).findById(new UsuarioId(1L));
        verify(usuarioRepository).save(any(User.class));
        verify(mapper).toDto(parcheado);
        verifyNoInteractions(passwordEncoder, barrioRepository);
    }

    @Test
    @DisplayName("con id inexistente lanza ResourceNotFoundException")
    void execute_conIdInexistente_lanzaResourceNotFoundException() {
        ParchearUsuarioCommand command = new ParchearUsuarioCommand(99L, "Juan", null, null, null, null, null);

        when(usuarioRepository.findById(new UsuarioId(99L))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> useCase.execute(command));

        verify(usuarioRepository).findById(new UsuarioId(99L));
        verify(usuarioRepository, never()).save(any());
        verifyNoInteractions(passwordEncoder, barrioRepository, mapper);
    }

    @Test
    @DisplayName("parchea email y password")
    void execute_parcheaEmailYPassword() {
        ParchearUsuarioCommand command = new ParchearUsuarioCommand(1L, null, "nuevo@test.com", null, null, "nuevopass", null);
        User existente = User.reconstruir(1L, "Juan", "juan@test.com", "3001234567", "Calle 1", "oldhash", 1L);
        User parcheado = User.reconstruir(1L, "Juan", "nuevo@test.com", "3001234567", "Calle 1", "newhash", 1L);
        UsuarioDTO dtoEsperado = new UsuarioDTO(1L, "Juan", "nuevo@test.com", "3001234567", "Calle 1", 1L);

        when(usuarioRepository.findById(new UsuarioId(1L))).thenReturn(Optional.of(existente));
        when(usuarioRepository.existsByEmail("nuevo@test.com")).thenReturn(false);
        when(passwordEncoder.hashear("nuevopass")).thenReturn("newhash");
        when(usuarioRepository.save(any(User.class))).thenReturn(parcheado);
        when(mapper.toDto(parcheado)).thenReturn(dtoEsperado);

        UsuarioDTO resultado = useCase.execute(command);

        assertNotNull(resultado);
        assertEquals("nuevo@test.com", resultado.email());

        verify(usuarioRepository).findById(new UsuarioId(1L));
        verify(usuarioRepository).existsByEmail("nuevo@test.com");
        verify(passwordEncoder).hashear("nuevopass");
        verify(usuarioRepository).save(any(User.class));
        verify(mapper).toDto(parcheado);
    }

    @Test
    @DisplayName("con email duplicado en parche lanza ResourceConflictException")
    void execute_conEmailDuplicado_lanzaResourceConflictException() {
        ParchearUsuarioCommand command = new ParchearUsuarioCommand(1L, null, "ocupado@test.com", null, null, null, null);
        User existente = User.reconstruir(1L, "Juan", "juan@test.com", "3001234567", "Calle 1", "hash", 1L);

        when(usuarioRepository.findById(new UsuarioId(1L))).thenReturn(Optional.of(existente));
        when(usuarioRepository.existsByEmail("ocupado@test.com")).thenReturn(true);

        assertThrows(ResourceConflictException.class, () -> useCase.execute(command));

        verify(usuarioRepository).findById(new UsuarioId(1L));
        verify(usuarioRepository).existsByEmail("ocupado@test.com");
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("con barrio inexistente en parche lanza ResourceNotFoundException")
    void execute_conBarrioInexistente_lanzaResourceNotFoundException() {
        ParchearUsuarioCommand command = new ParchearUsuarioCommand(1L, null, null, null, null, null, 99L);
        User existente = User.reconstruir(1L, "Juan", "juan@test.com", "3001234567", "Calle 1", "hash", 1L);

        when(usuarioRepository.findById(new UsuarioId(1L))).thenReturn(Optional.of(existente));
        when(barrioRepository.existsById(new BarrioId(99L))).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> useCase.execute(command));

        verify(usuarioRepository).findById(new UsuarioId(1L));
        verify(barrioRepository).existsById(new BarrioId(99L));
        verify(usuarioRepository, never()).save(any());
    }
}
