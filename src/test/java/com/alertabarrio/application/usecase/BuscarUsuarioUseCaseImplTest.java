package com.alertabarrio.application.usecase;

import com.alertabarrio.application.dto.UsuarioDTO;
import com.alertabarrio.application.mapper.UsuarioDomainMapper;
import com.alertabarrio.application.query.BuscarUsuarioQuery;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.model.User;
import com.alertabarrio.domain.model.valueobject.UsuarioId;
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
@DisplayName("BuscarUsuarioUseCaseImpl")
class BuscarUsuarioUseCaseImplTest {

    @Mock
    private UsuarioRepositoryPort usuarioRepository;

    @Mock
    private UsuarioDomainMapper mapper;

    @InjectMocks
    private BuscarUsuarioUseCaseImpl useCase;

    @Test
    @DisplayName("encuentra usuario por id y devuelve DTO")
    void execute_conIdValido_encuentraYDevuelveDTO() {
        BuscarUsuarioQuery query = new BuscarUsuarioQuery(1L);
        User usuario = User.reconstruir(1L, "Juan", "juan@test.com", "3001234567", "Calle 1", "hash", 1L);
        UsuarioDTO dtoEsperado = new UsuarioDTO(1L, "Juan", "juan@test.com", "3001234567", "Calle 1", 1L);

        when(usuarioRepository.findById(new UsuarioId(1L))).thenReturn(Optional.of(usuario));
        when(mapper.toDto(usuario)).thenReturn(dtoEsperado);

        UsuarioDTO resultado = useCase.execute(query);

        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        assertEquals("Juan", resultado.name());

        verify(usuarioRepository).findById(new UsuarioId(1L));
        verify(mapper).toDto(usuario);
    }

    @Test
    @DisplayName("con id inexistente lanza ResourceNotFoundException")
    void execute_conIdInexistente_lanzaResourceNotFoundException() {
        BuscarUsuarioQuery query = new BuscarUsuarioQuery(99L);

        when(usuarioRepository.findById(new UsuarioId(99L))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> useCase.execute(query));

        verify(usuarioRepository).findById(new UsuarioId(99L));
        verifyNoInteractions(mapper);
    }
}
