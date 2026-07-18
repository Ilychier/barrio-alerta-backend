package com.alertabarrio.ingsoft.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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

    @Test
    void shouldThrowExceptionWhenAlertaDoesNotExist() {

        when(alertaRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> alertaService.findById(99L)
        );
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingAlerta() {

        when(alertaRepository.findById(10L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> alertaService.update(
                        10L,
                        new AlertaSaveDTO(
                                "Nueva descripción",
                                true,
                                1L,
                                2L
                        )
                )
        );

        verify(alertaRepository, never()).save(any(Alerta.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingWithNonExistingUser() {

        Categoria categoria = buildCategoria(2L);
        Alerta alerta = buildAlerta(10L, buildUser(1L), categoria);

        when(alertaRepository.findById(10L))
                .thenReturn(Optional.of(alerta));

        when(userRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> alertaService.update(
                        10L,
                        new AlertaSaveDTO(
                                "Nueva descripción",
                                true,
                                99L,
                                2L
                        )
                )
        );

        verify(alertaRepository, never()).save(any(Alerta.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingWithNonExistingCategoria() {

        User user = buildUser(1L);
        Alerta alerta = buildAlerta(10L, user, null);

        when(alertaRepository.findById(10L))
                .thenReturn(Optional.of(alerta));

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(categoriaRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> alertaService.update(
                        10L,
                        new AlertaSaveDTO(
                                "Nueva descripción",
                                true,
                                1L,
                                99L
                        )
                )
        );

        verify(alertaRepository, never()).save(any(Alerta.class));
    }

    @Test
    void shouldThrowExceptionWhenPatchingNonExistingAlerta() {

        when(alertaRepository.findById(10L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> alertaService.patch(
                        10L,
                        new AlertaSaveDTO(
                                "Actualizada",
                                null,
                                null,
                                null
                        )
                )
        );

        verify(alertaRepository, never()).save(any(Alerta.class));
    }

    @Test
    void shouldThrowExceptionWhenPatchingWithNonExistingUser() {

        User user = buildUser(1L);

        Alerta alerta = buildAlerta(10L, user, null);

        when(alertaRepository.findById(10L))
                .thenReturn(Optional.of(alerta));

        when(userRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> alertaService.patch(
                        10L,
                        new AlertaSaveDTO(
                                null,
                                null,
                                99L,
                                null
                        )
                )
        );

        verify(alertaRepository, never()).save(any(Alerta.class));
    }

    @Test
    void shouldThrowExceptionWhenPatchingWithNonExistingCategoria() {

        User user = buildUser(1L);

        Alerta alerta = buildAlerta(10L, user, null);

        when(alertaRepository.findById(10L))
                .thenReturn(Optional.of(alerta));

        when(categoriaRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> alertaService.patch(
                        10L,
                        new AlertaSaveDTO(
                                null,
                                null,
                                null,
                                99L
                        )
                )
        );

        verify(alertaRepository, never()).save(any(Alerta.class));
    }

    @Test
    void shouldReturnPaginatedAlertas() {

        User user = buildUser(1L);
        Categoria categoria = buildCategoria(2L);

        Alerta alerta = buildAlerta(10L, user, categoria);

        Page<Alerta> page =
                new org.springframework.data.domain.PageImpl<>(
                        java.util.List.of(alerta)
                );

        when(alertaRepository.findAll(any(Pageable.class)))
                .thenReturn(page);

        Page<AlertaResponseDTO> result =
                alertaService.findAllPaginated(
                        org.springframework.data.domain.PageRequest.of(0, 10)
                );

        assertFalse(result.isEmpty());
        assertEquals(1, result.getContent().size());

        AlertaResponseDTO dto = result.getContent().get(0);

        assertEquals(10L, dto.id());
        assertEquals("Descripción de prueba", dto.descripcion());
    }

    @Test
    void shouldFindAlertasByFecha() {

        User user = buildUser(1L);
        Categoria categoria = buildCategoria(2L);

        Alerta alerta = buildAlerta(10L, user, categoria);
        alerta.setFechaHora(
                LocalDateTime.of(
                        2025,
                        1,
                        15,
                        10,
                        30
                )
        );

        Page<Alerta> page =
                new PageImpl<>(java.util.List.of(alerta));

        when(alertaRepository.findByFechaHoraBetween(
                any(LocalDateTime.class),
                any(LocalDateTime.class),
                any(Pageable.class)
        )).thenReturn(page);

        Page<AlertaResponseDTO> result =
                alertaService.findByFecha(
                        LocalDate.of(2025, 1, 15),
                        PageRequest.of(0, 10)
                );

        assertEquals(1, result.getContent().size());
        assertEquals(10L, result.getContent().get(0).id());
    }

    @Test
    void shouldFindAlertasByBarrioAndFecha() {

        User user = buildUser(1L);

        com.alertabarrio.ingsoft.models.entities.Barrio barrio =
                new com.alertabarrio.ingsoft.models.entities.Barrio();

        barrio.setId(5L);

        user.setBarrio(barrio);

        Categoria categoria = buildCategoria(2L);

        Alerta alerta = buildAlerta(10L, user, categoria);

        alerta.setFechaHora(
                LocalDateTime.of(
                        2025,
                        1,
                        15,
                        10,
                        30
                )
        );

        Page<Alerta> page =
                new PageImpl<>(java.util.List.of(alerta));

        when(alertaRepository.findByUsuario_Barrio_IdAndFechaHoraBetween(
                eq(5L),
                any(LocalDateTime.class),
                any(LocalDateTime.class),
                any(Pageable.class)
        )).thenReturn(page);

        Page<AlertaResponseDTO> result =
                alertaService.findByBarrioAndFecha(
                        5L,
                        LocalDate.of(2025, 1, 15),
                        PageRequest.of(0, 10)
                );

        assertFalse(result.isEmpty());
        assertEquals(1, result.getContent().size());
        assertEquals(10L, result.getContent().get(0).id());
    }
}