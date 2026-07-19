package com.alertabarrio.ingsoft.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.alertabarrio.ingsoft.exceptions.ResourceConflictException;
import com.alertabarrio.ingsoft.exceptions.ResourceNotFoundException;
import com.alertabarrio.ingsoft.models.dtos.CategoriaResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.CategoriaSaveDTO;
import com.alertabarrio.ingsoft.repositories.CategoriaRepository;
import com.alertabarrio.ingsoft.services.CategoriaService;

@SpringBootTest
@Transactional
class CategoriaIntegrationTest {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Test
    void shouldCreateCategoriaSuccessfully() {

        CategoriaResponseDTO created = categoriaService.save(
            new CategoriaSaveDTO(
                "Seguridad",
                "icono-seguridad"
            )
        );

        assertNotNull(created);
        assertNotNull(created.id());
        assertEquals("Seguridad", created.nombre());
        assertEquals("icono-seguridad", created.iconoReferencia());
    }

    @Test
    void shouldFindCategoriaByIdSuccessfully() {

        CategoriaResponseDTO created = categoriaService.save(
            new CategoriaSaveDTO(
                "Emergencia",
                "icono-emergencia"
            )
        );

        CategoriaResponseDTO found =
            categoriaService.findById(created.id());

        assertEquals(created.id(), found.id());
        assertEquals("Emergencia", found.nombre());
        assertEquals("icono-emergencia", found.iconoReferencia());
    }

    @Test
    void shouldUpdateCategoriaSuccessfully() {

        CategoriaResponseDTO created = categoriaService.save(
            new CategoriaSaveDTO(
                "Original",
                "icono-original"
            )
        );

        CategoriaResponseDTO updated =
            categoriaService.update(
                created.id(),
                new CategoriaSaveDTO(
                    "Actualizada",
                    "icono-actualizado"
                )
            );

        assertEquals("Actualizada", updated.nombre());
        assertEquals("icono-actualizado", updated.iconoReferencia());
    }

    @Test
    void shouldPatchCategoriaSuccessfully() {

        CategoriaResponseDTO created = categoriaService.save(
            new CategoriaSaveDTO(
                "Salud",
                "icono-salud"
            )
        );

        CategoriaResponseDTO patched =
            categoriaService.patch(
                created.id(),
                new CategoriaSaveDTO(
                    "Salud Actualizada",
                    null
                )
            );

        assertEquals("Salud Actualizada", patched.nombre());
        assertEquals("icono-salud", patched.iconoReferencia());
    }

    @Test
    void shouldDeleteCategoriaSuccessfully() {

        CategoriaResponseDTO created = categoriaService.save(
            new CategoriaSaveDTO(
                "Temporal",
                "icono-temporal"
            )
        );

        categoriaService.delete(created.id());

        assertThrows(
            ResourceNotFoundException.class,
            () -> categoriaService.findById(created.id())
        );
    }

    @Test
    void shouldThrowExceptionWhenFindingNonExistingCategoria() {

        assertThrows(
            ResourceNotFoundException.class,
            () -> categoriaService.findById(99999L)
        );
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingCategoria() {

        assertThrows(
            ResourceNotFoundException.class,
            () -> categoriaService.update(
                99999L,
                new CategoriaSaveDTO(
                    "Nueva",
                    "icono"
                )
            )
        );
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingCategoria() {

        assertThrows(
            ResourceNotFoundException.class,
            () -> categoriaService.delete(99999L)
        );
    }

    @Test
    void shouldThrowExceptionWhenCreatingDuplicateCategoria() {

        categoriaService.save(
            new CategoriaSaveDTO(
                "Duplicada",
                "icono-1"
            )
        );

        assertThrows(
            ResourceConflictException.class,
            () -> categoriaService.save(
                new CategoriaSaveDTO(
                    "Duplicada",
                    "icono-2"
                )
            )
        );
    }

    @Test
    void shouldThrowExceptionWhenUpdatingCategoriaWithExistingName() {

        CategoriaResponseDTO categoria1 =
            categoriaService.save(
                new CategoriaSaveDTO(
                    "Categoria A",
                    "icono-a"
                )
            );

        categoriaService.save(
            new CategoriaSaveDTO(
                "Categoria B",
                "icono-b"
            )
        );

        assertThrows(
            ResourceConflictException.class,
            () -> categoriaService.update(
                categoria1.id(),
                new CategoriaSaveDTO(
                    "Categoria B",
                    "otro-icono"
                )
            )
        );
    }
}