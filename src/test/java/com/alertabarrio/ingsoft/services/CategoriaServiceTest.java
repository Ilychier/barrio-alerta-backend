package com.alertabarrio.ingsoft.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
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

import com.alertabarrio.ingsoft.exceptions.ResourceConflictException;
import com.alertabarrio.ingsoft.exceptions.ResourceNotFoundException;
import com.alertabarrio.ingsoft.models.dtos.CategoriaResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.CategoriaSaveDTO;
import com.alertabarrio.ingsoft.models.entities.Categoria;
import com.alertabarrio.ingsoft.repositories.CategoriaRepository;
import com.alertabarrio.ingsoft.services.implementation.CategoriaServiceImpl;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaServiceImpl categoriaService;

    @Test
    void shouldSaveCategoriaSuccessfully() {

        CategoriaSaveDTO dto =
            new CategoriaSaveDTO(
                "Seguridad",
                "icono-seguridad"
            );

        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Seguridad");
        categoria.setIconoReferencia("icono-seguridad");

        when(categoriaRepository.existsByNombre("Seguridad"))
            .thenReturn(false);

        when(categoriaRepository.save(any(Categoria.class)))
            .thenReturn(categoria);

        CategoriaResponseDTO result =
            categoriaService.save(dto);

        assertEquals(1L, result.id());
        assertEquals("Seguridad", result.nombre());
        assertEquals("icono-seguridad", result.iconoReferencia());
    }

    @Test
    void shouldThrowExceptionWhenSavingDuplicatedCategoria() {

        when(categoriaRepository.existsByNombre("Seguridad"))
            .thenReturn(true);

        assertThrows(
            ResourceConflictException.class,
            () -> categoriaService.save(
                new CategoriaSaveDTO(
                    "Seguridad",
                    "icono"
                )
            )
        );
    }
    
    @Test
    void shouldFindCategoriaByIdSuccessfully() {

        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Seguridad");
        categoria.setIconoReferencia("icono");

        when(categoriaRepository.findById(1L))
            .thenReturn(Optional.of(categoria));

        CategoriaResponseDTO result =
            categoriaService.findById(1L);

        assertEquals(1L, result.id());
        assertEquals("Seguridad", result.nombre());
    }

    @Test
    void shouldThrowExceptionWhenCategoriaNotFound() {

        when(categoriaRepository.findById(999L))
            .thenReturn(Optional.empty());

        assertThrows(
            ResourceNotFoundException.class,
            () -> categoriaService.findById(999L)
        );
    }

    @Test
    void shouldUpdateCategoriaSuccessfully() {

        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Seguridad");
        categoria.setIconoReferencia("icono1");

        CategoriaSaveDTO dto =
            new CategoriaSaveDTO(
                "Emergencia",
                "icono2"
            );

        when(categoriaRepository.findById(1L))
            .thenReturn(Optional.of(categoria));

        when(categoriaRepository.existsByNombre("Emergencia"))
            .thenReturn(false);

        when(categoriaRepository.save(any(Categoria.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        CategoriaResponseDTO result =
            categoriaService.update(1L, dto);

        assertEquals("Emergencia", result.nombre());
        assertEquals("icono2", result.iconoReferencia());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingCategoria() {

        when(categoriaRepository.findById(999L))
            .thenReturn(Optional.empty());

        assertThrows(
            ResourceNotFoundException.class,
            () -> categoriaService.update(
                999L,
                new CategoriaSaveDTO(
                    "Nueva",
                    "icono"
                )
            )
        );
    }

    @Test
    void shouldThrowExceptionWhenUpdatingDuplicatedCategoria() {

        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Seguridad");

        when(categoriaRepository.findById(1L))
            .thenReturn(Optional.of(categoria));

        when(categoriaRepository.existsByNombre("Emergencia"))
            .thenReturn(true);

        assertThrows(
            ResourceConflictException.class,
            () -> categoriaService.update(
                1L,
                new CategoriaSaveDTO(
                    "Emergencia",
                    "icono"
                )
            )
        );
    }

    @Test
    void shouldUpdateCategoriaKeepingSameName() {

        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Seguridad");
        categoria.setIconoReferencia("icono1");

        when(categoriaRepository.findById(1L))
            .thenReturn(Optional.of(categoria));

        when(categoriaRepository.save(any(Categoria.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        CategoriaResponseDTO result =
            categoriaService.update(
                1L,
                new CategoriaSaveDTO(
                    "Seguridad",
                    "icono-actualizado"
                )
            );

        assertEquals("Seguridad", result.nombre());
        assertEquals("icono-actualizado", result.iconoReferencia());
    }

    @Test
    void shouldPatchCategoriaSuccessfully() {

        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Seguridad");
        categoria.setIconoReferencia("icono1");

        when(categoriaRepository.findById(1L))
            .thenReturn(Optional.of(categoria));

        when(categoriaRepository.existsByNombre("Emergencia"))
            .thenReturn(false);

        when(categoriaRepository.save(any(Categoria.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        CategoriaResponseDTO result =
            categoriaService.patch(
                1L,
                new CategoriaSaveDTO(
                    "Emergencia",
                    null
                )
            );

        assertEquals("Emergencia", result.nombre());
        assertEquals("icono1", result.iconoReferencia());
    }

    @Test
    void shouldPatchOnlyIconoSuccessfully() {

        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Seguridad");
        categoria.setIconoReferencia("icono1");

        when(categoriaRepository.findById(1L))
            .thenReturn(Optional.of(categoria));

        when(categoriaRepository.save(any(Categoria.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        CategoriaResponseDTO result =
            categoriaService.patch(
                1L,
                new CategoriaSaveDTO(
                    null,
                    "icono2"
                )
            );

        assertEquals("Seguridad", result.nombre());
        assertEquals("icono2", result.iconoReferencia());
    }

    @Test
    void shouldPatchNombreAndIconoSuccessfully() {

        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Seguridad");
        categoria.setIconoReferencia("icono1");

        when(categoriaRepository.findById(1L))
            .thenReturn(Optional.of(categoria));

        when(categoriaRepository.existsByNombre("Emergencia"))
            .thenReturn(false);

        when(categoriaRepository.save(any(Categoria.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        CategoriaResponseDTO result =
            categoriaService.patch(
                1L,
                new CategoriaSaveDTO(
                    "Emergencia",
                    "icono2"
                )
            );

        assertEquals("Emergencia", result.nombre());
        assertEquals("icono2", result.iconoReferencia());
    }

    @Test
    void shouldThrowExceptionWhenPatchingNonExistingCategoria() {

        when(categoriaRepository.findById(999L))
            .thenReturn(Optional.empty());

        assertThrows(
            ResourceNotFoundException.class,
            () -> categoriaService.patch(
                999L,
                new CategoriaSaveDTO(
                    "Nueva",
                    "icono"
                )
            )
        );
    }

    @Test
    void shouldThrowExceptionWhenPatchingDuplicatedCategoria() {

        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Seguridad");

        when(categoriaRepository.findById(1L))
            .thenReturn(Optional.of(categoria));

        when(categoriaRepository.existsByNombre("Emergencia"))
            .thenReturn(true);

        assertThrows(
            ResourceConflictException.class,
            () -> categoriaService.patch(
                1L,
                new CategoriaSaveDTO(
                    "Emergencia",
                    null
                )
            )
        );
    }

    @Test
    void shouldDeleteCategoriaSuccessfully() {

        when(categoriaRepository.existsById(1L))
            .thenReturn(true);

        categoriaService.delete(1L);

        verify(categoriaRepository).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingCategoria() {

        when(categoriaRepository.existsById(999L))
            .thenReturn(false);

        assertThrows(
            ResourceNotFoundException.class,
            () -> categoriaService.delete(999L)
        );

        verify(categoriaRepository, never())
            .deleteById(any(Long.class));
    }

    @Test
    void shouldReturnPaginatedCategorias() {

        Categoria categoria1 = new Categoria();
        categoria1.setId(1L);
        categoria1.setNombre("Seguridad");
        categoria1.setIconoReferencia("icono1");

        Categoria categoria2 = new Categoria();
        categoria2.setId(2L);
        categoria2.setNombre("Salud");
        categoria2.setIconoReferencia("icono2");

        Page<Categoria> page =
            new PageImpl<>(
                List.of(categoria1, categoria2)
            );

        when(categoriaRepository.findAll(any(Pageable.class)))
            .thenReturn(page);

        Page<CategoriaResponseDTO> result =
            categoriaService.findAllPaginated(
                PageRequest.of(0, 10)
            );

        assertFalse(result.isEmpty());
        assertEquals(2, result.getContent().size());
    }

    @Test
    void shouldReturnEmptyPage() {

        Page<Categoria> emptyPage =
            new PageImpl<>(List.of());

        when(categoriaRepository.findAll(any(Pageable.class)))
            .thenReturn(emptyPage);

        Page<CategoriaResponseDTO> result =
            categoriaService.findAllPaginated(
                PageRequest.of(0, 10)
            );

        assertTrue(result.isEmpty());
    }
}
