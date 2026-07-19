package com.alertabarrio.ingsoft.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.alertabarrio.ingsoft.exceptions.ResourceNotFoundException;
import com.alertabarrio.ingsoft.models.dtos.CategoriaDescripcionResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.CategoriaDescripcionSaveDTO;
import com.alertabarrio.ingsoft.models.entities.Categoria;
import com.alertabarrio.ingsoft.models.entities.CategoriaDescripcion;
import com.alertabarrio.ingsoft.repositories.CategoriaDescripcionRepository;
import com.alertabarrio.ingsoft.repositories.CategoriaRepository;
import com.alertabarrio.ingsoft.services.implementation.CategoriaDescripcionServiceImpl;

@ExtendWith(MockitoExtension.class)
class CategoriaDescripcionServiceTest {

    @Mock
    private CategoriaDescripcionRepository categoriaDescripcionRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaDescripcionServiceImpl categoriaDescripcionService;

    private Categoria categoria;
    private CategoriaDescripcion categoriaDescripcion;

    @BeforeEach
    void setUp() {

        categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Seguridad");
        categoria.setIconoReferencia("icono");

        categoriaDescripcion = new CategoriaDescripcion();
        categoriaDescripcion.setId(1L);
        categoriaDescripcion.setDescripcion("Descripción inicial");
        categoriaDescripcion.setCategoria(categoria);
        categoriaDescripcion.setImagenUrl("imagen.jpg");
    }

    @Test
    void shouldSaveSuccessfully() {

        CategoriaDescripcionSaveDTO dto =
            new CategoriaDescripcionSaveDTO(
                "Descripción inicial",
                1L,
                "imagen.jpg"
            );

        when(categoriaRepository.findById(1L))
            .thenReturn(Optional.of(categoria));

        when(categoriaDescripcionRepository.save(any(CategoriaDescripcion.class)))
            .thenReturn(categoriaDescripcion);

        CategoriaDescripcionResponseDTO result =
            categoriaDescripcionService.save(dto);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Descripción inicial", result.descripcion());
    }

    @Test
    void shouldThrowWhenCategoriaDoesNotExistOnSave() {

        when(categoriaRepository.findById(99L))
            .thenReturn(Optional.empty());

        assertThrows(
            ResourceNotFoundException.class,
            () -> categoriaDescripcionService.save(
                new CategoriaDescripcionSaveDTO(
                    "Desc",
                    99L,
                    null
                )
            )
        );
    }

    @Test
    void shouldFindByIdSuccessfully() {

        when(categoriaDescripcionRepository.findById(1L))
            .thenReturn(Optional.of(categoriaDescripcion));

        CategoriaDescripcionResponseDTO result =
            categoriaDescripcionService.findById(1L);

        assertEquals(1L, result.id());
        assertEquals("Descripción inicial", result.descripcion());
    }

    @Test
    void shouldThrowWhenFindByIdNotExists() {

        when(categoriaDescripcionRepository.findById(999L))
            .thenReturn(Optional.empty());

        assertThrows(
            ResourceNotFoundException.class,
            () -> categoriaDescripcionService.findById(999L)
        );
    }

    @Test
    void shouldUpdateSuccessfully() {

        Categoria nuevaCategoria = new Categoria();
        nuevaCategoria.setId(2L);

        CategoriaDescripcionSaveDTO dto =
            new CategoriaDescripcionSaveDTO(
                "Descripción actualizada",
                2L,
                "nueva.jpg"
            );

        when(categoriaDescripcionRepository.findById(1L))
            .thenReturn(Optional.of(categoriaDescripcion));

        when(categoriaRepository.findById(2L))
            .thenReturn(Optional.of(nuevaCategoria));

        when(categoriaDescripcionRepository.save(any(CategoriaDescripcion.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        CategoriaDescripcionResponseDTO result =
            categoriaDescripcionService.update(1L, dto);

        assertEquals("Descripción actualizada", result.descripcion());
        assertEquals(2L, result.categoriaId());
    }

    @Test
    void shouldThrowWhenUpdatingNonExistingCategoriaDescripcion() {

        when(categoriaDescripcionRepository.findById(999L))
            .thenReturn(Optional.empty());

        assertThrows(
            ResourceNotFoundException.class,
            () -> categoriaDescripcionService.update(
                999L,
                new CategoriaDescripcionSaveDTO(
                    "Desc",
                    1L,
                    null
                )
            )
        );
    }

    @Test
    void shouldPatchSuccessfully() {

        Categoria nuevaCategoria = new Categoria();
        nuevaCategoria.setId(2L);

        CategoriaDescripcionSaveDTO dto =
            new CategoriaDescripcionSaveDTO(
                "Descripción modificada",
                2L,
                "patch.jpg"
            );

        when(categoriaDescripcionRepository.findById(1L))
            .thenReturn(Optional.of(categoriaDescripcion));

        when(categoriaRepository.findById(2L))
            .thenReturn(Optional.of(nuevaCategoria));

        when(categoriaDescripcionRepository.save(any(CategoriaDescripcion.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        CategoriaDescripcionResponseDTO result =
            categoriaDescripcionService.patch(1L, dto);

        assertEquals("Descripción modificada", result.descripcion());
        assertEquals("patch.jpg", result.imagenUrl());
        assertEquals(2L, result.categoriaId());
    }

    @Test
    void shouldDeleteSuccessfully() {

        when(categoriaDescripcionRepository.existsById(1L))
            .thenReturn(true);

        categoriaDescripcionService.delete(1L);

        verify(categoriaDescripcionRepository)
            .deleteById(1L);
    }

    @Test
    void shouldThrowWhenDeletingNonExistingCategoriaDescripcion() {

        when(categoriaDescripcionRepository.existsById(999L))
            .thenReturn(false);

        assertThrows(
            ResourceNotFoundException.class,
            () -> categoriaDescripcionService.delete(999L)
        );
    }
}