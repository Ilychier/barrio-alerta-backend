package com.alertabarrio.domain.model;

import com.alertabarrio.domain.exception.CategoriaDescripcionInvalidaException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CategoriaDescripcion (dominio puro)")
class CategoriaDescripcionTest {

    @Nested
    @DisplayName("crear()")
    class Crear {

        @Test
        @DisplayName("con datos válidos retorna CategoriaDescripcion sin id")
        void conDatosValidos_retornaCategoriaDescripcion() {
            CategoriaDescripcion cd = CategoriaDescripcion.crear("Zona peligrosa", 1L, "img.jpg");

            assertNull(cd.getId());
            assertEquals("Zona peligrosa", cd.getDescripcion());
            assertEquals(1L, cd.getCategoriaId().value());
            assertEquals("img.jpg", cd.getImagenUrl());
        }

        @Test
        @DisplayName("con descripcion nula lanza CategoriaDescripcionInvalidaException")
        void conDescripcionNula_lanzaExcepcion() {
            assertThrows(CategoriaDescripcionInvalidaException.class,
                    () -> CategoriaDescripcion.crear(null, 1L, "img.jpg"));
        }

        @Test
        @DisplayName("con descripcion vacía lanza CategoriaDescripcionInvalidaException")
        void conDescripcionVacia_lanzaExcepcion() {
            assertThrows(CategoriaDescripcionInvalidaException.class,
                    () -> CategoriaDescripcion.crear("   ", 1L, "img.jpg"));
        }

        @Test
        @DisplayName("con categoriaId nulo lanza CategoriaDescripcionInvalidaException")
        void conCategoriaIdNulo_lanzaExcepcion() {
            assertThrows(CategoriaDescripcionInvalidaException.class,
                    () -> CategoriaDescripcion.crear("Zona peligrosa", null, "img.jpg"));
        }

        @Test
        @DisplayName("con imagenUrl nula no lanza excepción")
        void conImagenUrlNula_noLanzaExcepcion() {
            CategoriaDescripcion cd = CategoriaDescripcion.crear("Zona peligrosa", 1L, null);
            assertNull(cd.getImagenUrl());
        }

        @Test
        @DisplayName("recorta espacios en blanco al inicio y final")
        void recortaEspacios() {
            CategoriaDescripcion cd = CategoriaDescripcion.crear("  Zona peligrosa  ", 1L, "  img.jpg  ");
            assertEquals("Zona peligrosa", cd.getDescripcion());
            assertEquals("img.jpg", cd.getImagenUrl());
        }
    }

    @Nested
    @DisplayName("reconstruir()")
    class Reconstruir {

        @Test
        @DisplayName("con datos válidos retorna CategoriaDescripcion con id")
        void conDatosValidos_retornaCategoriaDescripcionConId() {
            CategoriaDescripcion cd = CategoriaDescripcion.reconstruir(1L, "Zona peligrosa", 2L, "img.jpg");

            assertNotNull(cd.getId());
            assertEquals(1L, cd.getId().value());
            assertEquals("Zona peligrosa", cd.getDescripcion());
            assertEquals(2L, cd.getCategoriaId().value());
            assertEquals("img.jpg", cd.getImagenUrl());
        }

        @Test
        @DisplayName("con descripcion nula lanza CategoriaDescripcionInvalidaException")
        void conDescripcionNula_lanzaExcepcion() {
            assertThrows(CategoriaDescripcionInvalidaException.class,
                    () -> CategoriaDescripcion.reconstruir(1L, null, 2L, "img.jpg"));
        }

        @Test
        @DisplayName("con categoriaId nulo lanza CategoriaDescripcionInvalidaException")
        void conCategoriaIdNulo_lanzaExcepcion() {
            assertThrows(CategoriaDescripcionInvalidaException.class,
                    () -> CategoriaDescripcion.reconstruir(1L, "Zona peligrosa", null, "img.jpg"));
        }
    }
}
