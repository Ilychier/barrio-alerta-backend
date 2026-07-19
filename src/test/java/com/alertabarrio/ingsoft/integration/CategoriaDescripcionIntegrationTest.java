package com.alertabarrio.ingsoft.integration;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.alertabarrio.ingsoft.exceptions.ResourceNotFoundException;
import com.alertabarrio.ingsoft.models.dtos.CategoriaDescripcionResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.CategoriaDescripcionSaveDTO;
import com.alertabarrio.ingsoft.models.entities.Categoria;
import com.alertabarrio.ingsoft.repositories.CategoriaRepository;
import com.alertabarrio.ingsoft.services.CategoriaDescripcionService;

@SpringBootTest
@Transactional
class CategoriaDescripcionIntegrationTest {

    @Autowired
    private CategoriaDescripcionService categoriaDescripcionService;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Test
    void shouldCreateCategoriaDescripcionSuccessfully() {

        Categoria categoria = crearCategoria("Seguridad");

        CategoriaDescripcionResponseDTO created =
            categoriaDescripcionService.save(
                new CategoriaDescripcionSaveDTO(
                    "Descripción de seguridad",
                    categoria.getId(),
                    "imagen.jpg"
                )
            );

        assertNotNull(created);
        assertNotNull(created.id());
        assertEquals("Descripción de seguridad", created.descripcion());
        assertEquals(categoria.getId(), created.categoriaId());
        assertEquals("imagen.jpg", created.imagenUrl());
    }

    @Test
    void shouldFindCategoriaDescripcionByIdSuccessfully() {

        Categoria categoria = crearCategoria("Emergencia");

        CategoriaDescripcionResponseDTO created =
            categoriaDescripcionService.save(
                new CategoriaDescripcionSaveDTO(
                    "Descripción emergencia",
                    categoria.getId(),
                    "emergencia.jpg"
                )
            );

        CategoriaDescripcionResponseDTO found =
            categoriaDescripcionService.findById(created.id());

        assertEquals(created.id(), found.id());
        assertEquals("Descripción emergencia", found.descripcion());
        assertEquals(categoria.getId(), found.categoriaId());
    }

    @Test
    void shouldUpdateCategoriaDescripcionSuccessfully() {

        Categoria categoria = crearCategoria("Salud");

        CategoriaDescripcionResponseDTO created =
            categoriaDescripcionService.save(
                new CategoriaDescripcionSaveDTO(
                    "Descripción original",
                    categoria.getId(),
                    "original.jpg"
                )
            );

        CategoriaDescripcionResponseDTO updated =
            categoriaDescripcionService.update(
                created.id(),
                new CategoriaDescripcionSaveDTO(
                    "Descripción actualizada",
                    categoria.getId(),
                    "actualizada.jpg"
                )
            );

        assertEquals("Descripción actualizada", updated.descripcion());
        assertEquals("actualizada.jpg", updated.imagenUrl());
    }

    @Test
    void shouldPatchCategoriaDescripcionSuccessfully() {

        Categoria categoria = crearCategoria("Transporte");

        CategoriaDescripcionResponseDTO created =
            categoriaDescripcionService.save(
                new CategoriaDescripcionSaveDTO(
                    "Descripción transporte",
                    categoria.getId(),
                    "vieja.jpg"
                )
            );

        CategoriaDescripcionResponseDTO patched =
            categoriaDescripcionService.patch(
                created.id(),
                new CategoriaDescripcionSaveDTO(
                    null,
                    null,
                    "nueva.jpg"
                )
            );

        assertEquals("Descripción transporte", patched.descripcion());
        assertEquals("nueva.jpg", patched.imagenUrl());
    }

    @Test
    void shouldDeleteCategoriaDescripcionSuccessfully() {

        Categoria categoria = crearCategoria("Vial");

        CategoriaDescripcionResponseDTO created =
            categoriaDescripcionService.save(
                new CategoriaDescripcionSaveDTO(
                    "Descripción vial",
                    categoria.getId(),
                    "vial.jpg"
                )
            );

        categoriaDescripcionService.delete(created.id());

        assertThrows(
            ResourceNotFoundException.class,
            () -> categoriaDescripcionService.findById(created.id())
        );
    }

    @Test
    void shouldThrowExceptionWhenFindingNonExistingCategoriaDescripcion() {

        assertThrows(
            ResourceNotFoundException.class,
            () -> categoriaDescripcionService.findById(999L)
        );
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingCategoriaDescripcion() {

        Categoria categoria = crearCategoria("Prueba");

        assertThrows(
            ResourceNotFoundException.class,
            () -> categoriaDescripcionService.update(
                999L,
                new CategoriaDescripcionSaveDTO(
                    "Desc",
                    categoria.getId(),
                    null
                )
            )
        );
    }

    private Categoria crearCategoria(String nombre) {

        Categoria categoria = new Categoria();
        categoria.setNombre(nombre);
        categoria.setIconoReferencia("icono-" + nombre.toLowerCase());

        return categoriaRepository.save(categoria);
    }
}
