package com.alertabarrio.ingsoft.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.alertabarrio.ingsoft.exceptions.ResourceNotFoundException;
import com.alertabarrio.ingsoft.models.dtos.AlertaResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.AlertaSaveDTO;
import com.alertabarrio.ingsoft.models.entities.Alerta;
import com.alertabarrio.ingsoft.models.entities.Categoria;
import com.alertabarrio.ingsoft.models.entities.User;
import com.alertabarrio.ingsoft.repositories.AlertaRepository;
import com.alertabarrio.ingsoft.repositories.CategoriaRepository;
import com.alertabarrio.ingsoft.repositories.UserRepository;
import com.alertabarrio.ingsoft.services.implementation.AlertaServicelmpl;

@ExtendWith(MockitoExtension.class)
class AlertaServiceTest {

    @Mock
    private AlertaRepository alertaRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private AlertaServicelmpl alertaService;

    @Test
    void shouldSaveAlertaSuccessfully() {
        User user = buildUser(1L);
        Categoria categoria = buildCategoria(2L);

        Alerta saved = buildAlerta(10L, user, categoria);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(categoriaRepository.findById(2L)).thenReturn(Optional.of(categoria));
        when(alertaRepository.save(any(Alerta.class))).thenReturn(saved);

        AlertaResponseDTO result = alertaService.save(
            new AlertaSaveDTO("Descripción de prueba", true, 1L, 2L)
        );

        assertEquals(10L, result.id());
        assertEquals("Descripción de prueba", result.descripcion());
        assertTrue(result.esSos());
        assertEquals(1L, result.usuarioId());
        assertNotNull(result.categoria());
        assertEquals(2L, result.categoria().id());
        verify(alertaRepository).save(any(Alerta.class));
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(
            ResourceNotFoundException.class,
            () -> alertaService.save(new AlertaSaveDTO("Desc", false, 99L, null))
        );

        verify(alertaRepository, never()).save(any(Alerta.class));
    }

    @Test
    void shouldThrowExceptionWhenCategoriaDoesNotExist() {
        User user = buildUser(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(categoriaRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(
            ResourceNotFoundException.class,
            () -> alertaService.save(new AlertaSaveDTO("Desc", false, 1L, 2L))
        );

        verify(alertaRepository, never()).save(any(Alerta.class));
    }

    @Test
    void shouldFindAlertaByIdSuccessfully() {
        User user = buildUser(1L);
        Categoria categoria = buildCategoria(2L);
        Alerta alerta = buildAlerta(10L, user, categoria);

        when(alertaRepository.findById(10L)).thenReturn(Optional.of(alerta));

        AlertaResponseDTO result = alertaService.findById(10L);

        assertEquals(10L, result.id());
        assertEquals("Descripción de prueba", result.descripcion());
        assertEquals(1L, result.usuarioId());
        assertEquals(2L, result.categoria().id());
    }

    @Test
    void shouldDeleteAlertaSuccessfully() {
        when(alertaRepository.existsById(10L)).thenReturn(true);

        alertaService.delete(10L);

        verify(alertaRepository).deleteById(10L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingAlerta() {
        when(alertaRepository.existsById(10L)).thenReturn(false);

        assertThrows(
            ResourceNotFoundException.class,
            () -> alertaService.delete(10L)
        );

        verify(alertaRepository, never()).deleteById(10L);
    }

    @Test
    void shouldUpdateAlertaSuccessfully() {
        User user = buildUser(1L);
        Categoria categoria = buildCategoria(2L);
        Alerta existing = buildAlerta(10L, user, null);
        existing.setDescripcion("Vieja");
        existing.setEsSos(false);

        when(alertaRepository.findById(10L)).thenReturn(Optional.of(existing));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(categoriaRepository.findById(2L)).thenReturn(Optional.of(categoria));
        when(alertaRepository.save(any(Alerta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AlertaResponseDTO result = alertaService.update(
            10L,
            new AlertaSaveDTO("Nueva descripción", true, 1L, 2L)
        );

        assertEquals("Nueva descripción", result.descripcion());
        assertTrue(result.esSos());
        assertEquals(2L, result.categoria().id());
    }

    @Test
    void shouldPatchAlertaSuccessfully() {
        User user = buildUser(1L);
        Categoria categoria = buildCategoria(2L);
        Alerta existing = buildAlerta(10L, user, null);
        existing.setDescripcion("Vieja");
        existing.setEsSos(false);

        when(alertaRepository.findById(10L)).thenReturn(Optional.of(existing));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(categoriaRepository.findById(2L)).thenReturn(Optional.of(categoria));
        when(alertaRepository.save(any(Alerta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AlertaResponseDTO result = alertaService.patch(
            10L,
            new AlertaSaveDTO("Actualizada", null, 1L, 2L)
        );

        assertEquals("Actualizada", result.descripcion());
        assertFalse(result.esSos());
        assertEquals(2L, result.categoria().id());
    }

    private User buildUser(Long id) {
        User user = new User();
        user.setId(id);
        return user;
    }

    private Categoria buildCategoria(Long id) {
        Categoria categoria = new Categoria();
        categoria.setId(id);
        categoria.setNombre("Seguridad");
        categoria.setIconoReferencia("icono");
        return categoria;
    }

    private Alerta buildAlerta(Long id, User user, Categoria categoria) {
        Alerta alerta = new Alerta();
        alerta.setId(id);
        alerta.setDescripcion("Descripción de prueba");
        alerta.setEsSos(true);
        alerta.setUsuario(user);
        alerta.setCategoria(categoria);
        return alerta;
    }
}