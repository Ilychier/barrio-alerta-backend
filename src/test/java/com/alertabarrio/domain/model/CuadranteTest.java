package com.alertabarrio.domain.model;

import com.alertabarrio.domain.exception.CuadranteInvalidaException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Cuadrante (dominio puro)")
class CuadranteTest {

    @Nested
    @DisplayName("crear()")
    class Crear {

        @Test
        @DisplayName("con datos válidos retorna Cuadrante sin id")
        void conDatosValidos_retornaCuadrante() {
            Cuadrante cuadrante = Cuadrante.crear("Bomberos", "+573001234567");

            assertNull(cuadrante.getId());
            assertEquals("Bomberos", cuadrante.getNombreUnidad());
            assertEquals("+573001234567", cuadrante.getTelefonoEmergencia());
        }

        @Test
        @DisplayName("con nombre nulo lanza CuadranteInvalidaException")
        void conNombreNulo_lanzaExcepcion() {
            assertThrows(CuadranteInvalidaException.class,
                    () -> Cuadrante.crear(null, "+573001234567"));
        }

        @Test
        @DisplayName("con nombre vacío lanza CuadranteInvalidaException")
        void conNombreVacio_lanzaExcepcion() {
            assertThrows(CuadranteInvalidaException.class,
                    () -> Cuadrante.crear("   ", "+573001234567"));
        }

        @Test
        @DisplayName("con nombre que excede 100 caracteres lanza CuadranteInvalidaException")
        void conNombreExcede100Chars_lanzaExcepcion() {
            String nombreLargo = "a".repeat(101);
            assertThrows(CuadranteInvalidaException.class,
                    () -> Cuadrante.crear(nombreLargo, "+573001234567"));
        }

        @Test
        @DisplayName("con teléfono nulo lanza CuadranteInvalidaException")
        void conTelefonoNulo_lanzaExcepcion() {
            assertThrows(CuadranteInvalidaException.class,
                    () -> Cuadrante.crear("Bomberos", null));
        }

        @Test
        @DisplayName("con teléfono vacío lanza CuadranteInvalidaException")
        void conTelefonoVacio_lanzaExcepcion() {
            assertThrows(CuadranteInvalidaException.class,
                    () -> Cuadrante.crear("Bomberos", "   "));
        }

        @Test
        @DisplayName("con teléfono que excede 100 caracteres lanza CuadranteInvalidaException")
        void conTelefonoExcede100Chars_lanzaExcepcion() {
            String telefonoLargo = "5".repeat(101);
            assertThrows(CuadranteInvalidaException.class,
                    () -> Cuadrante.crear("Bomberos", telefonoLargo));
        }

        @Test
        @DisplayName("recorta espacios en blanco al inicio y final")
        void recortaEspacios() {
            Cuadrante cuadrante = Cuadrante.crear("  Bomberos  ", "  +573001234567  ");
            assertEquals("Bomberos", cuadrante.getNombreUnidad());
            assertEquals("+573001234567", cuadrante.getTelefonoEmergencia());
        }
    }

    @Nested
    @DisplayName("reconstruir()")
    class Reconstruir {

        @Test
        @DisplayName("con datos válidos retorna Cuadrante con id")
        void conDatosValidos_retornaCuadranteConId() {
            Cuadrante cuadrante = Cuadrante.reconstruir(1L, "Bomberos", "+573001234567");

            assertNotNull(cuadrante.getId());
            assertEquals(1L, cuadrante.getId().value());
            assertEquals("Bomberos", cuadrante.getNombreUnidad());
            assertEquals("+573001234567", cuadrante.getTelefonoEmergencia());
        }

        @Test
        @DisplayName("con nombre nulo lanza CuadranteInvalidaException")
        void conNombreNulo_lanzaExcepcion() {
            assertThrows(CuadranteInvalidaException.class,
                    () -> Cuadrante.reconstruir(1L, null, "+573001234567"));
        }

        @Test
        @DisplayName("con teléfono nulo lanza CuadranteInvalidaException")
        void conTelefonoNulo_lanzaExcepcion() {
            assertThrows(CuadranteInvalidaException.class,
                    () -> Cuadrante.reconstruir(1L, "Bomberos", null));
        }
    }
}
