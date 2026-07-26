package com.alertabarrio.application.usecase;

import com.alertabarrio.application.dto.UsuarioDTO;
import com.alertabarrio.application.mapper.UsuarioDomainMapper;
import com.alertabarrio.application.query.ListarUsuariosQuery;
import com.alertabarrio.domain.model.User;
import com.alertabarrio.domain.port.out.UsuarioRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ListarUsuariosUseCaseImpl")
class ListarUsuariosUseCaseImplTest {

    @Mock
    private UsuarioRepositoryPort usuarioRepository;

    @Mock
    private UsuarioDomainMapper mapper;

    @InjectMocks
    private ListarUsuariosUseCaseImpl useCase;

    @Test
    @DisplayName("devuelve página de usuarios")
    void execute_conQueryValido_devuelvePagina() {
        Pageable pageable = PageRequest.of(0, 10);
        ListarUsuariosQuery query = new ListarUsuariosQuery(pageable);

        User usuario1 = User.reconstruir(1L, "Juan", "juan@test.com", "3001234567", "Calle 1", "hash", 1L);
        User usuario2 = User.reconstruir(2L, "Ana", "ana@test.com", "3007654321", "Calle 2", "hash", 2L);
        Page<User> paginaUsuarios = new PageImpl<>(List.of(usuario1, usuario2), pageable, 2);

        UsuarioDTO dto1 = new UsuarioDTO(1L, "Juan", "juan@test.com", "3001234567", "Calle 1", 1L);
        UsuarioDTO dto2 = new UsuarioDTO(2L, "Ana", "ana@test.com", "3007654321", "Calle 2", 2L);

        when(usuarioRepository.findAll(pageable)).thenReturn(paginaUsuarios);
        when(mapper.toDto(usuario1)).thenReturn(dto1);
        when(mapper.toDto(usuario2)).thenReturn(dto2);

        Page<UsuarioDTO> resultado = useCase.execute(query);

        assertNotNull(resultado);
        assertEquals(2, resultado.getTotalElements());
        assertEquals(2, resultado.getContent().size());
        assertEquals("Juan", resultado.getContent().get(0).name());
        assertEquals("Ana", resultado.getContent().get(1).name());

        verify(usuarioRepository).findAll(pageable);
        verify(mapper).toDto(usuario1);
        verify(mapper).toDto(usuario2);
    }

    @Test
    @DisplayName("devuelve página vacía cuando no hay usuarios")
    void execute_sinUsuarios_devuelvePaginaVacia() {
        Pageable pageable = PageRequest.of(0, 10);
        ListarUsuariosQuery query = new ListarUsuariosQuery(pageable);
        Page<User> paginaVacia = Page.empty(pageable);

        when(usuarioRepository.findAll(pageable)).thenReturn(paginaVacia);

        Page<UsuarioDTO> resultado = useCase.execute(query);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(usuarioRepository).findAll(pageable);
        verifyNoInteractions(mapper);
    }
}
