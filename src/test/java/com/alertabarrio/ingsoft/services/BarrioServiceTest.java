package com.alertabarrio.ingsoft.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

import com.alertabarrio.ingsoft.exceptions.ResourceConflictException;
import com.alertabarrio.ingsoft.exceptions.ResourceNotFoundException;
import com.alertabarrio.ingsoft.models.dtos.BarrioResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.BarrioSaveDTO;
import com.alertabarrio.ingsoft.models.entities.Barrio;
import com.alertabarrio.ingsoft.models.entities.Cuadrante;
import com.alertabarrio.ingsoft.repositories.BarrioRepository;
import com.alertabarrio.ingsoft.repositories.CuadranteRepository;
import com.alertabarrio.ingsoft.services.implementation.BarrioServiceImpl;

@ExtendWith(MockitoExtension.class)
class BarrioServiceTest {

    @Mock
    private BarrioRepository barrioRepository;

    @Mock
    private CuadranteRepository cuadranteRepository;

    @InjectMocks
    private BarrioServiceImpl barrioService;

    @Test
    void shouldSaveBarrioSuccessfully() {

        Cuadrante cuadrante = buildCuadrante(1L);

        Barrio saved = buildBarrio(10L, "Centro", cuadrante);

        when(barrioRepository.existsByNombre("Centro")).thenReturn(false);
        when(cuadranteRepository.findById(1L)).thenReturn(Optional.of(cuadrante));
        when(barrioRepository.save(any(Barrio.class))).thenReturn(saved);

        BarrioResponseDTO result =
                barrioService.save(new BarrioSaveDTO("Centro", 1L));

        assertEquals(10L, result.id());
        assertEquals("Centro", result.nombre());
        assertNotNull(result.cuadrante());
        assertEquals(1L, result.cuadrante().id());

        verify(barrioRepository).save(any(Barrio.class));
    }

    @Test
    void shouldThrowExceptionWhenBarrioNameAlreadyExists() {

        when(barrioRepository.existsByNombre("Centro"))
                .thenReturn(true);

        assertThrows(
                ResourceConflictException.class,
                () -> barrioService.save(
                        new BarrioSaveDTO("Centro", 1L))
        );

        verify(barrioRepository, never()).save(any(Barrio.class));
    }

    @Test
    void shouldThrowExceptionWhenCuadranteDoesNotExistOnSave() {

        when(barrioRepository.existsByNombre("Centro"))
                .thenReturn(false);

        when(cuadranteRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> barrioService.save(
                        new BarrioSaveDTO("Centro", 1L))
        );

        verify(barrioRepository, never()).save(any(Barrio.class));
    }

    @Test
    void shouldFindBarrioByIdSuccessfully() {

        Cuadrante cuadrante = buildCuadrante(1L);
        Barrio barrio = buildBarrio(10L, "Centro", cuadrante);

        when(barrioRepository.findById(10L))
                .thenReturn(Optional.of(barrio));

        BarrioResponseDTO result =
                barrioService.findById(10L);

        assertEquals(10L, result.id());
        assertEquals("Centro", result.nombre());
    }

    @Test
    void shouldThrowExceptionWhenBarrioDoesNotExist() {

        when(barrioRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> barrioService.findById(999L)
        );
    }

    @Test
    void shouldUpdateBarrioSuccessfully() {

        Cuadrante cuadrante = buildCuadrante(1L);
        Barrio barrio = buildBarrio(10L, "Centro", cuadrante);

        when(barrioRepository.findById(10L))
                .thenReturn(Optional.of(barrio));

        when(barrioRepository.existsByNombre("Nuevo Centro"))
                .thenReturn(false);

        when(cuadranteRepository.findById(1L))
                .thenReturn(Optional.of(cuadrante));

        when(barrioRepository.save(any(Barrio.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        BarrioResponseDTO result =
                barrioService.update(
                        10L,
                        new BarrioSaveDTO("Nuevo Centro", 1L)
                );

        assertEquals("Nuevo Centro", result.nombre());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingBarrio() {

        when(barrioRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> barrioService.update(
                        999L,
                        new BarrioSaveDTO("Centro", 1L)
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenUpdatingWithExistingName() {

        Cuadrante cuadrante = buildCuadrante(1L);
        Barrio barrio = buildBarrio(10L, "Centro", cuadrante);

        when(barrioRepository.findById(10L))
                .thenReturn(Optional.of(barrio));

        when(barrioRepository.existsByNombre("Otro Barrio"))
                .thenReturn(true);

        assertThrows(
                ResourceConflictException.class,
                () -> barrioService.update(
                        10L,
                        new BarrioSaveDTO("Otro Barrio", 1L)
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenUpdatingWithNonExistingCuadrante() {

        Cuadrante cuadrante = buildCuadrante(1L);
        Barrio barrio = buildBarrio(10L, "Centro", cuadrante);

        when(barrioRepository.findById(10L))
                .thenReturn(Optional.of(barrio));

        when(barrioRepository.existsByNombre("Nuevo Centro"))
                .thenReturn(false);

        when(cuadranteRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> barrioService.update(
                        10L,
                        new BarrioSaveDTO("Nuevo Centro", 99L)
                )
        );
    }

    @Test
    void shouldPatchBarrioNameSuccessfully() {

        Cuadrante cuadrante = buildCuadrante(1L);
        Barrio barrio = buildBarrio(10L, "Centro", cuadrante);

        when(barrioRepository.findById(10L))
                .thenReturn(Optional.of(barrio));

        when(barrioRepository.existsByNombre("Actualizado"))
                .thenReturn(false);

        when(barrioRepository.save(any(Barrio.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        BarrioResponseDTO result =
                barrioService.patch(
                        10L,
                        new BarrioSaveDTO("Actualizado", null)
                );

        assertEquals("Actualizado", result.nombre());
    }

    @Test
    void shouldPatchCuadranteSuccessfully() {

        Cuadrante viejo = buildCuadrante(1L);
        Cuadrante nuevo = buildCuadrante(2L);

        Barrio barrio = buildBarrio(10L, "Centro", viejo);

        when(barrioRepository.findById(10L))
                .thenReturn(Optional.of(barrio));

        when(cuadranteRepository.findById(2L))
                .thenReturn(Optional.of(nuevo));

        when(barrioRepository.save(any(Barrio.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        BarrioResponseDTO result =
                barrioService.patch(
                        10L,
                        new BarrioSaveDTO(null, 2L)
                );

        assertEquals(2L, result.cuadrante().id());
    }

    @Test
    void shouldDeleteBarrioSuccessfully() {

        when(barrioRepository.existsById(10L))
                .thenReturn(true);

        barrioService.delete(10L);

        verify(barrioRepository).deleteById(10L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingBarrio() {

        when(barrioRepository.existsById(10L))
                .thenReturn(false);

        assertThrows(
                ResourceNotFoundException.class,
                () -> barrioService.delete(10L)
        );

        verify(barrioRepository, never()).deleteById(10L);
    }

    private Barrio buildBarrio(Long id, String nombre, Cuadrante cuadrante) {

        Barrio barrio = new Barrio();
        barrio.setId(id);
        barrio.setNombre(nombre);
        barrio.setCuadrante(cuadrante);

        return barrio;
    }

    private Cuadrante buildCuadrante(Long id) {

        Cuadrante cuadrante = new Cuadrante();
        cuadrante.setId(id);
        cuadrante.setNombreUnidad("Unidad");
        cuadrante.setTelefonoEmergencia("123456");

        return cuadrante;
    }
}