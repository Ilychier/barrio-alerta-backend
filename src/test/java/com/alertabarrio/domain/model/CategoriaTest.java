package com.alertabarrio.domain.model;

import com.alertabarrio.domain.exception.CategoriaInvalidaException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Categoria (dominio puro)")
class CategoriaTest {

    @Nested
    @DisplayName("crear()")
    class Crear {

        @Test
        @DisplayName("con datos válidos retorna Categoria sin id")
        void conDatosValidos_retornaCategoria() {
            Categoria categoria = Categoria.crear("Robo", "icon-robbery.png");

            assertNull(categoria.getId());
            assertEquals("Robo", categoria.getNombre());
            assertEquals("icon-robbery.png", categoria.getIconoReferencia());
        }

        @Test
        @DisplayName("con nombre nulo lanza CategoriaInvalidaException")
        void conNombreNulo_lanzaExcepcion() {
            assertThrows(CategoriaInvalidaException.class,
                    () -> Categoria.crear(null, "icon.png"));
        }

        @Test
        @DisplayName("con nombre vacío lanza CategoriaInvalidaException")
        void conNombreVacio_lanzaExcepcion() {
            assertThrows(CategoriaInvalidaException.class,
                    () -> Categoria.crear("   ", "icon.png"));
        }

        @Test
        @DisplayName("con nombre que excede 100 caracteres lanza CategoriaInvalidaException")
        void conNombreExcede100Chars_lanzaExcepcion() {
            String nombreLargo = "a".repeat(101);
            assertThrows(CategoriaInvalidaException.class,
                    () -> Categoria.crear(nombreLargo, "icon.png"));
        }

        @Test
        @DisplayName("con icono nulo lanza CategoriaInvalidaException")
        void conIconoNulo_lanzaExcepcion() {
            assertThrows(CategoriaInvalidaException.class,
                    () -> Categoria.crear("Robo", null));
        }

        @Test
        @DisplayName("con icono vacío lanza CategoriaInvalidaException")
        void conIconoVacio_lanzaExcepcion() {
            assertThrows(CategoriaInvalidaException.class,
                    () -> Categoria.crear("Robo", "   "));
        }

        @Test
        @DisplayName("con icono que excede 255 caracteres lanza CategoriaInvalidaException")
        void conIconoExcede255Chars_lanzaExcepcion() {
            String iconoLargo = "a".repeat(256);
            assertThrows(CategoriaInvalidaException.class,
                    () -> Categoria.crear("Robo", iconoLargo));
        }

        @Test
        @DisplayName("recorta espacios en blanco al inicio y final")
        void recortaEspacios() {
            Categoria categoria = Categoria.crear("  Robo  ", "  icon.png  ");
            assertEquals("Robo", categoria.getNombre());
            assertEquals("icon.png", categoria.getIconoReferencia());
        }
    }

    @Nested
    @DisplayName("reconstruir()")
    class Reconstruir {

        @Test
        @DisplayName("con datos válidos retorna Categoria con id")
        void conDatosValidos_retornaCategoriaConId() {
            Categoria categoria = Categoria.reconstruir(1L, "Robo", "icon.png");

            assertNotNull(categoria.getId());
            assertEquals(1L, categoria.getId().value());
            assertEquals("Robo", categoria.getNombre());
            assertEquals("icon.png", categoria.getIconoReferencia());
        }

        @Test
        @DisplayName("con nombre nulo lanza CategoriaInvalidaException")
        void conNombreNulo_lanzaExcepcion() {
            assertThrows(CategoriaInvalidaException.class,
                    () -> Categoria.reconstruir(1L, null, "icon.png"));
        }

        @Test
        @DisplayName("con icono nulo lanza CategoriaInvalidaException")
        void conIconoNulo_lanzaExcepcion() {
            assertThrows(CategoriaInvalidaException.class,
                    () -> Categoria.reconstruir(1L, "Robo", null));
        }
    }
}
