package com.alertabarrio.domain.model.mascotas;

import com.alertabarrio.domain.exception.mascotas.TipoMascotaInvalidoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TipoMascota (dominio puro BC Mascotas)")
class TipoMascotaTest {

    @Nested
    @DisplayName("crear()")
    class Crear {

        @Test
        @DisplayName("con datos válidos retorna TipoMascota sin id y activo")
        void conDatosValidos_retornaTipoMascota() {
            TipoMascota tipo = TipoMascota.crear("Perro");

            assertNull(tipo.getId());
            assertEquals("Perro", tipo.getNombre());
            assertTrue(tipo.isActivo());
        }

        @Test
        @DisplayName("con nombre nulo lanza TipoMascotaInvalidoException")
        void conNombreNulo_lanzaExcepcion() {
            assertThrows(TipoMascotaInvalidoException.class,
                    () -> TipoMascota.crear(null));
        }

        @Test
        @DisplayName("con nombre vacío lanza TipoMascotaInvalidoException")
        void conNombreVacio_lanzaExcepcion() {
            assertThrows(TipoMascotaInvalidoException.class,
                    () -> TipoMascota.crear("   "));
        }

        @Test
        @DisplayName("con nombre que excede 50 caracteres lanza TipoMascotaInvalidoException")
        void conNombreExcede50Chars_lanzaExcepcion() {
            String nombreLargo = "a".repeat(51);
            assertThrows(TipoMascotaInvalidoException.class,
                    () -> TipoMascota.crear(nombreLargo));
        }

        @Test
        @DisplayName("recorta espacios en blanco al inicio y final")
        void recortaEspacios() {
            TipoMascota tipo = TipoMascota.crear("  Gato  ");
            assertEquals("Gato", tipo.getNombre());
        }
    }

    @Nested
    @DisplayName("reconstruir()")
    class Reconstruir {

        @Test
        @DisplayName("con datos válidos retorna TipoMascota con id")
        void conDatosValidos_retornaTipoMascotaConId() {
            TipoMascota tipo = TipoMascota.reconstruir(1L, "Perro", true);

            assertNotNull(tipo.getId());
            assertEquals(1L, tipo.getId().value());
            assertEquals("Perro", tipo.getNombre());
            assertTrue(tipo.isActivo());
        }

        @Test
        @DisplayName("con activo false retorna TipoMascota inactiva")
        void conActivoFalse_retornaInactiva() {
            TipoMascota tipo = TipoMascota.reconstruir(2L, "Conejo", false);
            assertFalse(tipo.isActivo());
        }

        @Test
        @DisplayName("con nombre nulo lanza TipoMascotaInvalidoException")
        void conNombreNulo_lanzaExcepcion() {
            assertThrows(TipoMascotaInvalidoException.class,
                    () -> TipoMascota.reconstruir(1L, null, true));
        }
    }
}
