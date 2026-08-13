package com.alertabarrio.domain.model;

import com.alertabarrio.domain.exception.UsuarioInvalidoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User (dominio puro)")
class UserTest {

    @Nested
    @DisplayName("crearRapido()")
    class CrearRapido {

        @Test
        @DisplayName("con datos válidos crea usuario temporal sin barrio")
        void conDatosValidos_creaUsuarioTemporal() {
            User user = User.crearRapido("+573001112233", "hashTemporal", "573001112233@mascotas.temp");

            assertEquals("Vecino", user.getName());
            assertEquals("+573001112233", user.getPhone().value());
            assertEquals("573001112233@mascotas.temp", user.getEmail().value());
            assertEquals("Sin dirección", user.getAddress());
            assertNull(user.getBarrioId());
            assertTrue(user.isPasswordTemporal());
        }

        @Test
        @DisplayName("con password vacía lanza UsuarioInvalidoException")
        void conPasswordVacia_lanzaExcepcion() {
            assertThrows(UsuarioInvalidoException.class,
                    () -> User.crearRapido("+573001112233", "   ", "x@mascotas.temp"));
        }

        @Test
        @DisplayName("con teléfono inválido lanza IllegalArgumentException (VO Telefono)")
        void conTelefonoInvalido_lanzaExcepcion() {
            assertThrows(IllegalArgumentException.class,
                    () -> User.crearRapido("no-es-telefono", "pass123", "x@mascotas.temp"));
        }

        @Test
        @DisplayName("con email sintético inválido lanza IllegalArgumentException (VO Email)")
        void conEmailInvalido_lanzaExcepcion() {
            assertThrows(IllegalArgumentException.class,
                    () -> User.crearRapido("+573001112233", "pass123", "no-es-email"));
        }
    }

    @Nested
    @DisplayName("crear()")
    class Crear {

        @Test
        @DisplayName("el registro completo no es temporal")
        void conDatosValidos_noEsTemporal() {
            User user = User.crear("Juan", "juan@test.com", "+573001111111", "Calle 1", "pass123", 1L);
            assertFalse(user.isPasswordTemporal());
        }
    }

    @Nested
    @DisplayName("cambiarPassword()")
    class CambiarPassword {

        @Test
        @DisplayName("cambia la password y quita el flag temporal (inmutable)")
        void cambiaPasswordYQuitaTemporal() {
            User user = User.crearRapido("+573001112233", "hashTemporal", "x@mascotas.temp");
            User actualizado = user.cambiarPassword("nuevaHash");

            assertEquals("nuevaHash", actualizado.getPassword());
            assertFalse(actualizado.isPasswordTemporal());
            assertTrue(user.isPasswordTemporal());
        }

        @Test
        @DisplayName("con password vacía lanza UsuarioInvalidoException")
        void conPasswordVacia_lanzaExcepcion() {
            User user = User.crearRapido("+573001112233", "hashTemporal", "x@mascotas.temp");
            assertThrows(UsuarioInvalidoException.class,
                    () -> user.cambiarPassword("   "));
        }
    }

    @Nested
    @DisplayName("reconstruir()")
    class Reconstruir {

        @Test
        @DisplayName("conserva el flag passwordTemporal")
        void conservaFlagTemporal() {
            User user = User.reconstruir(1L, "Vecino", "x@mascotas.temp", "+573001112233",
                    "Sin dirección", "hash", null, true);
            assertTrue(user.isPasswordTemporal());
            assertNull(user.getBarrioId());
        }

        @Test
        @DisplayName("con barrio null no lanza excepción (usuario rápido)")
        void conBarrioNull_noLanza() {
            assertDoesNotThrow(() -> User.reconstruir(1L, "Vecino", "x@mascotas.temp",
                    "+573001112233", "Sin dirección", "hash", null, false));
        }
    }
}
