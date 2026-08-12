package com.alertabarrio.domain.model.mascotas;

import com.alertabarrio.domain.exception.mascotas.CiudadInvalidaException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Ciudad (dominio puro BC Mascotas)")
class CiudadTest {

    @Nested
    @DisplayName("crear()")
    class Crear {

        @Test
        @DisplayName("con datos válidos retorna Ciudad sin id")
        void conDatosValidos_retornaCiudad() {
            Ciudad ciudad = Ciudad.crear("Cali", "Valle del Cauca", "Colombia");

            assertNull(ciudad.getId());
            assertEquals("Cali", ciudad.getNombre());
            assertEquals("Valle del Cauca", ciudad.getDepartamento());
            assertEquals("Colombia", ciudad.getPais());
        }

        @Test
        @DisplayName("con nombre nulo lanza CiudadInvalidaException")
        void conNombreNulo_lanzaExcepcion() {
            assertThrows(CiudadInvalidaException.class,
                    () -> Ciudad.crear(null, "Valle del Cauca", "Colombia"));
        }

        @Test
        @DisplayName("con nombre vacío lanza CiudadInvalidaException")
        void conNombreVacio_lanzaExcepcion() {
            assertThrows(CiudadInvalidaException.class,
                    () -> Ciudad.crear("   ", "Valle del Cauca", "Colombia"));
        }

        @Test
        @DisplayName("con nombre que excede 100 caracteres lanza CiudadInvalidaException")
        void conNombreExcede100Chars_lanzaExcepcion() {
            String nombreLargo = "a".repeat(101);
            assertThrows(CiudadInvalidaException.class,
                    () -> Ciudad.crear(nombreLargo, "Valle del Cauca", "Colombia"));
        }

        @Test
        @DisplayName("con departamento nulo lanza CiudadInvalidaException")
        void conDepartamentoNulo_lanzaExcepcion() {
            assertThrows(CiudadInvalidaException.class,
                    () -> Ciudad.crear("Cali", null, "Colombia"));
        }

        @Test
        @DisplayName("con departamento vacío lanza CiudadInvalidaException")
        void conDepartamentoVacio_lanzaExcepcion() {
            assertThrows(CiudadInvalidaException.class,
                    () -> Ciudad.crear("Cali", "  ", "Colombia"));
        }

        @Test
        @DisplayName("con país nulo lanza CiudadInvalidaException")
        void conPaisNulo_lanzaExcepcion() {
            assertThrows(CiudadInvalidaException.class,
                    () -> Ciudad.crear("Cali", "Valle del Cauca", null));
        }

        @Test
        @DisplayName("recorta espacios en blanco al inicio y final")
        void recortaEspacios() {
            Ciudad ciudad = Ciudad.crear("  Cali  ", "  Valle del Cauca  ", "  Colombia  ");
            assertEquals("Cali", ciudad.getNombre());
            assertEquals("Valle del Cauca", ciudad.getDepartamento());
            assertEquals("Colombia", ciudad.getPais());
        }
    }

    @Nested
    @DisplayName("reconstruir()")
    class Reconstruir {

        @Test
        @DisplayName("con datos válidos retorna Ciudad con id")
        void conDatosValidos_retornaCiudadConId() {
            Ciudad ciudad = Ciudad.reconstruir(1L, "Quibdó", "Chocó", "Colombia");

            assertNotNull(ciudad.getId());
            assertEquals(1L, ciudad.getId().value());
            assertEquals("Quibdó", ciudad.getNombre());
            assertEquals("Chocó", ciudad.getDepartamento());
        }

        @Test
        @DisplayName("con nombre nulo lanza CiudadInvalidaException")
        void conNombreNulo_lanzaExcepcion() {
            assertThrows(CiudadInvalidaException.class,
                    () -> Ciudad.reconstruir(1L, null, "Chocó", "Colombia"));
        }

        @Test
        @DisplayName("con departamento nulo lanza CiudadInvalidaException")
        void conDepartamentoNulo_lanzaExcepcion() {
            assertThrows(CiudadInvalidaException.class,
                    () -> Ciudad.reconstruir(1L, "Quibdó", null, "Colombia"));
        }
    }
}
