package com.alertabarrio.domain.model.mascotas;

import com.alertabarrio.domain.exception.mascotas.ReporteMascotaInvalidoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ReporteMascota (dominio puro BC Mascotas)")
class ReporteMascotaTest {

    private static final Instant INSTANTE_FIJO =
            Instant.parse("2026-08-11T10:00:00Z");
    private static final Clock RELOJ = Clock.fixed(INSTANTE_FIJO, ZoneId.of("America/Bogota"));

    private static final String TIPO_LOST = "LOST";
    private static final String TIPO_FOUND = "FOUND";
    private static final Long TIPO_MASCOTA_ID = 1L;
    private static final Long CIUDAD_ID = 1L;
    private static final String UBICACION = "Barrio La Soledad, cerca al parque";
    private static final String TELEFONO = "+573001234567";
    private static final String DESCRIPCION = "Perro criollo, collar rojo";
    private static final Long USUARIO_ID = 10L;

    @Nested
    @DisplayName("crear()")
    class Crear {

        @Test
        @DisplayName("con datos válidos retorna ReporteMascota sin id, estado ACTIVE y timestamps del reloj")
        void conDatosValidos_retornaReporte() {
            ReporteMascota reporte = ReporteMascota.crear(
                    TIPO_LOST, TIPO_MASCOTA_ID, CIUDAD_ID, UBICACION, TELEFONO, DESCRIPCION, USUARIO_ID, null, RELOJ);

            assertNull(reporte.getId());
            assertEquals(TipoReporte.LOST, reporte.getTipoReporte());
            assertEquals(TIPO_MASCOTA_ID, reporte.getTipoMascotaId().value());
            assertEquals(CIUDAD_ID, reporte.getCiudadId().value());
            assertEquals(UBICACION, reporte.getUbicacion());
            assertEquals(TELEFONO, reporte.getTelefono().value());
            assertEquals(DESCRIPCION, reporte.getDescripcion());
            assertEquals(EstadoReporte.ACTIVE, reporte.getEstado());
            assertEquals(LocalDateTime.now(RELOJ), reporte.getCreatedAt());
            assertEquals(LocalDateTime.now(RELOJ), reporte.getUpdatedAt());
            assertEquals(USUARIO_ID, reporte.getUsuarioId().value());
        }

        @Test
        @DisplayName("con tipo FOUND retorna tipo FOUND")
        void conTipoFound_retornaFound() {
            ReporteMascota reporte = ReporteMascota.crear(
                    TIPO_FOUND, TIPO_MASCOTA_ID, CIUDAD_ID, UBICACION, TELEFONO, null, USUARIO_ID, null, RELOJ);
            assertEquals(TipoReporte.FOUND, reporte.getTipoReporte());
        }

        @Test
        @DisplayName("con descripcion null permite descripcion null (fricción baja de entrada)")
        void conDescripcionNull_permitida() {
            ReporteMascota reporte = ReporteMascota.crear(
                    TIPO_LOST, TIPO_MASCOTA_ID, CIUDAD_ID, UBICACION, TELEFONO, null, USUARIO_ID, null, RELOJ);
            assertNull(reporte.getDescripcion());
        }

        @Test
        @DisplayName("con tipoReporte inválido lanza IllegalArgumentException")
        void conTipoInvalido_lanzaExcepcion() {
            assertThrows(IllegalArgumentException.class,
                    () -> ReporteMascota.crear(
                            "PERDIDO", TIPO_MASCOTA_ID, CIUDAD_ID, UBICACION, TELEFONO, DESCRIPCION, USUARIO_ID, null, RELOJ));
        }

        @Test
        @DisplayName("con tipoReporte en minúsculas normaliza a enum")
        void conTipoMinusculas_normaliza() {
            ReporteMascota reporte = ReporteMascota.crear(
                    "lost", TIPO_MASCOTA_ID, CIUDAD_ID, UBICACION, TELEFONO, DESCRIPCION, USUARIO_ID, null, RELOJ);
            assertEquals(TipoReporte.LOST, reporte.getTipoReporte());
        }

        @Test
        @DisplayName("con usuario null lanza ReporteMascotaInvalidoException")
        void conUsuarioNulo_lanzaExcepcion() {
            assertThrows(ReporteMascotaInvalidoException.class,
                    () -> ReporteMascota.crear(
                            TIPO_LOST, TIPO_MASCOTA_ID, CIUDAD_ID, UBICACION, TELEFONO, DESCRIPCION, null, null, RELOJ));
        }

        @Test
        @DisplayName("con tipoMascotaId null lanza ReporteMascotaInvalidoException")
        void conTipoMascotaNulo_lanzaExcepcion() {
            assertThrows(ReporteMascotaInvalidoException.class,
                    () -> ReporteMascota.crear(
                            TIPO_LOST, null, CIUDAD_ID, UBICACION, TELEFONO, DESCRIPCION, USUARIO_ID, null, RELOJ));
        }

        @Test
        @DisplayName("con ciudadId null lanza ReporteMascotaInvalidoException")
        void conCiudadNula_lanzaExcepcion() {
            assertThrows(ReporteMascotaInvalidoException.class,
                    () -> ReporteMascota.crear(
                            TIPO_LOST, TIPO_MASCOTA_ID, null, UBICACION, TELEFONO, DESCRIPCION, USUARIO_ID, null, RELOJ));
        }

        @Test
        @DisplayName("con ubicacion vacía lanza ReporteMascotaInvalidoException")
        void conUbicacionVacia_lanzaExcepcion() {
            assertThrows(ReporteMascotaInvalidoException.class,
                    () -> ReporteMascota.crear(
                            TIPO_LOST, TIPO_MASCOTA_ID, CIUDAD_ID, "   ", TELEFONO, DESCRIPCION, USUARIO_ID, null, RELOJ));
        }

        @Test
        @DisplayName("con ubicacion que excede 255 caracteres lanza ReporteMascotaInvalidoException")
        void conUbicacionExcede255Chars_lanzaExcepcion() {
            String ubicacionLarga = "a".repeat(256);
            assertThrows(ReporteMascotaInvalidoException.class,
                    () -> ReporteMascota.crear(
                            TIPO_LOST, TIPO_MASCOTA_ID, CIUDAD_ID, ubicacionLarga, TELEFONO, DESCRIPCION, USUARIO_ID, null, RELOJ));
        }

        @Test
        @DisplayName("con telefono inválido lanza IllegalArgumentException (VO Telefono)")
        void conTelefonoInvalido_lanzaExcepcion() {
            assertThrows(IllegalArgumentException.class,
                    () -> ReporteMascota.crear(
                            TIPO_LOST, TIPO_MASCOTA_ID, CIUDAD_ID, UBICACION, "no-es-un-telefono", DESCRIPCION, USUARIO_ID, null, RELOJ));
        }

        @Test
        @DisplayName("recorta espacios en ubicacion y descripcion")
        void recortaEspacios() {
            ReporteMascota reporte = ReporteMascota.crear(
                    TIPO_LOST, TIPO_MASCOTA_ID, CIUDAD_ID, "  Barrio La Soledad  ", TELEFONO, "  Perro criollo  ", USUARIO_ID, null, RELOJ);
            assertEquals("Barrio La Soledad", reporte.getUbicacion());
            assertEquals("Perro criollo", reporte.getDescripcion());
        }

        @Test
        @DisplayName("con otroTipoMascota lo conserva recortado")
        void conOtroTipoMascota_loConserva() {
            ReporteMascota reporte = ReporteMascota.crear(
                    TIPO_LOST, TIPO_MASCOTA_ID, CIUDAD_ID, UBICACION, TELEFONO, DESCRIPCION, USUARIO_ID, "  Conejo  ", RELOJ);
            assertEquals("Conejo", reporte.getOtroTipoMascota());
        }

        @Test
        @DisplayName("con otroTipoMascota null lo deja null (tipos estándar)")
        void conOtroTipoMascotaNull_permitido() {
            ReporteMascota reporte = ReporteMascota.crear(
                    TIPO_LOST, TIPO_MASCOTA_ID, CIUDAD_ID, UBICACION, TELEFONO, DESCRIPCION, USUARIO_ID, null, RELOJ);
            assertNull(reporte.getOtroTipoMascota());
        }

        @Test
        @DisplayName("con otroTipoMascota en blanco lo normaliza a null")
        void conOtroTipoMascotaBlanco_normalizaANull() {
            ReporteMascota reporte = ReporteMascota.crear(
                    TIPO_LOST, TIPO_MASCOTA_ID, CIUDAD_ID, UBICACION, TELEFONO, DESCRIPCION, USUARIO_ID, "   ", RELOJ);
            assertNull(reporte.getOtroTipoMascota());
        }

        @Test
        @DisplayName("con otroTipoMascota que excede 50 caracteres lanza ReporteMascotaInvalidoException")
        void conOtroTipoMascotaExcede50Chars_lanzaExcepcion() {
            String largo = "a".repeat(51);
            assertThrows(ReporteMascotaInvalidoException.class,
                    () -> ReporteMascota.crear(
                            TIPO_LOST, TIPO_MASCOTA_ID, CIUDAD_ID, UBICACION, TELEFONO, DESCRIPCION, USUARIO_ID, largo, RELOJ));
        }
    }

    @Nested
    @DisplayName("reconstruir()")
    class Reconstruir {

        @Test
        @DisplayName("con datos válidos retorna ReporteMascota con id y estado")
        void conDatosValidos_retornaReporteConId() {
            LocalDateTime fecha = LocalDateTime.now(RELOJ);
            ReporteMascota reporte = ReporteMascota.reconstruir(
                    1L, "LOST", TIPO_MASCOTA_ID, CIUDAD_ID, UBICACION, TELEFONO, DESCRIPCION,
                    "ACTIVE", fecha, fecha, USUARIO_ID, null);

            assertNotNull(reporte.getId());
            assertEquals(1L, reporte.getId().value());
            assertEquals(TipoReporte.LOST, reporte.getTipoReporte());
            assertEquals(EstadoReporte.ACTIVE, reporte.getEstado());
            assertEquals(fecha, reporte.getCreatedAt());
            assertEquals(fecha, reporte.getUpdatedAt());
        }

        @Test
        @DisplayName("con estado RESCUED retorna estado RESCUED")
        void conEstadoRescued_retornaRescued() {
            LocalDateTime fecha = LocalDateTime.now(RELOJ);
            ReporteMascota reporte = ReporteMascota.reconstruir(
                    1L, "LOST", TIPO_MASCOTA_ID, CIUDAD_ID, UBICACION, TELEFONO, DESCRIPCION,
                    "RESCUED", fecha, fecha, USUARIO_ID, null);
            assertEquals(EstadoReporte.RESCUED, reporte.getEstado());
        }

        @Test
        @DisplayName("con id null lanza ReporteMascotaInvalidoException")
        void conIdNulo_lanzaExcepcion() {
            LocalDateTime fecha = LocalDateTime.now(RELOJ);
            assertThrows(ReporteMascotaInvalidoException.class,
                    () -> ReporteMascota.reconstruir(
                            null, "LOST", TIPO_MASCOTA_ID, CIUDAD_ID, UBICACION, TELEFONO, DESCRIPCION,
                            "ACTIVE", fecha, fecha, USUARIO_ID, null));
        }

        @Test
        @DisplayName("con estado inválido lanza IllegalArgumentException")
        void conEstadoInvalido_lanzaExcepcion() {
            LocalDateTime fecha = LocalDateTime.now(RELOJ);
            assertThrows(IllegalArgumentException.class,
                    () -> ReporteMascota.reconstruir(
                            1L, "LOST", TIPO_MASCOTA_ID, CIUDAD_ID, UBICACION, TELEFONO, DESCRIPCION,
                            "ELIMINADO", fecha, fecha, USUARIO_ID, null));
        }
    }

    @Nested
    @DisplayName("cambiarEstado()")
    class CambiarEstado {

        private ReporteMascota reporteActivo() {
            return ReporteMascota.crear(
                    TIPO_LOST, TIPO_MASCOTA_ID, CIUDAD_ID, UBICACION, TELEFONO, DESCRIPCION, USUARIO_ID, null, RELOJ);
        }

        @Test
        @DisplayName("cambia a RESCUED retornando nueva instancia inmutable")
        void cambiaARescued_retornaNuevaInstancia() {
            ReporteMascota original = reporteActivo();
            ReporteMascota rescatado = original.cambiarEstado(EstadoReporte.RESCUED, RELOJ);

            assertEquals(EstadoReporte.ACTIVE, original.getEstado());
            assertEquals(EstadoReporte.RESCUED, rescatado.getEstado());
            assertNotSame(original, rescatado);
            assertEquals(original.getId(), rescatado.getId());
            assertEquals(original.getUsuarioId(), rescatado.getUsuarioId());
        }

        @Test
        @DisplayName("actualiza updatedAt en la nueva instancia")
        void actualizaUpdatedAt() {
            Instant despues = INSTANTE_FIJO.plusSeconds(3600);
            Clock relojDespues = Clock.fixed(despues, ZoneId.of("America/Bogota"));

            ReporteMascota original = reporteActivo();
            ReporteMascota rescatado = original.cambiarEstado(EstadoReporte.RESCUED, relojDespues);

            assertEquals(LocalDateTime.now(RELOJ), rescatado.getCreatedAt());
            assertEquals(LocalDateTime.now(relojDespues), rescatado.getUpdatedAt());
        }

        @Test
        @DisplayName("con estado null lanza ReporteMascotaInvalidoException")
        void conEstadoNulo_lanzaExcepcion() {
            ReporteMascota original = reporteActivo();
            assertThrows(ReporteMascotaInvalidoException.class,
                    () -> original.cambiarEstado(null, RELOJ));
        }

        @Test
        @DisplayName("un reporte DELETED no puede cambiar de estado (invariante)")
        void reporteEliminado_noPuedeCambiarEstado() {
            ReporteMascota original = reporteActivo();
            ReporteMascota eliminado = original.eliminar(RELOJ);

            assertEquals(EstadoReporte.DELETED, eliminado.getEstado());
            assertThrows(ReporteMascotaInvalidoException.class,
                    () -> eliminado.cambiarEstado(EstadoReporte.ACTIVE, RELOJ));
        }
    }

    @Nested
    @DisplayName("actualizar()")
    class Actualizar {

        @Test
        @DisplayName("actualiza campos editables retornando nueva instancia")
        void actualizaCamposEditables() {
            ReporteMascota original = ReporteMascota.crear(
                    TIPO_LOST, TIPO_MASCOTA_ID, CIUDAD_ID, UBICACION, TELEFONO, DESCRIPCION, USUARIO_ID, null, RELOJ);

            ReporteMascota actualizado = original.actualizar(
                    "Barrio El Prado, carrera 12", "+573009876543", "Ahora con correa azul", RELOJ);

            assertEquals("Barrio El Prado, carrera 12", actualizado.getUbicacion());
            assertEquals("+573009876543", actualizado.getTelefono().value());
            assertEquals("Ahora con correa azul", actualizado.getDescripcion());
            assertEquals(TipoReporte.LOST, actualizado.getTipoReporte());
            assertEquals(CIUDAD_ID, actualizado.getCiudadId().value());
            assertEquals(USUARIO_ID, actualizado.getUsuarioId().value());
            assertEquals(EstadoReporte.ACTIVE, actualizado.getEstado());
        }

        @Test
        @DisplayName("con ubicacion vacía lanza ReporteMascotaInvalidoException")
        void conUbicacionVacia_lanzaExcepcion() {
            ReporteMascota original = reporteActivo();
            assertThrows(ReporteMascotaInvalidoException.class,
                    () -> original.actualizar("   ", TELEFONO, DESCRIPCION, RELOJ));
        }

        private ReporteMascota reporteActivo() {
            return ReporteMascota.crear(
                    TIPO_LOST, TIPO_MASCOTA_ID, CIUDAD_ID, UBICACION, TELEFONO, DESCRIPCION, USUARIO_ID, null, RELOJ);
        }
    }

    @Nested
    @DisplayName("eliminar()")
    class Eliminar {

        @Test
        @DisplayName("retorna nueva instancia con estado DELETED (soft delete)")
        void eliminar_retornaEliminado() {
            ReporteMascota original = ReporteMascota.crear(
                    TIPO_LOST, TIPO_MASCOTA_ID, CIUDAD_ID, UBICACION, TELEFONO, DESCRIPCION, USUARIO_ID, null, RELOJ);

            ReporteMascota eliminado = original.eliminar(RELOJ);

            assertEquals(EstadoReporte.ACTIVE, original.getEstado());
            assertEquals(EstadoReporte.DELETED, eliminado.getEstado());
        }
    }

    @Nested
    @DisplayName("Enums del dominio")
    class Enums {

        @Test
        @DisplayName("TipoReporte.fromString normaliza a mayúsculas")
        void tipoReporteFromString_normaliza() {
            assertEquals(TipoReporte.LOST, TipoReporte.fromString("lost"));
            assertEquals(TipoReporte.FOUND, TipoReporte.fromString("found"));
        }

        @Test
        @DisplayName("TipoReporte.fromString con valor inválido lanza IllegalArgumentException")
        void tipoReporteFromString_invalido() {
            assertThrows(IllegalArgumentException.class,
                    () -> TipoReporte.fromString("PERDIDO"));
        }

        @Test
        @DisplayName("EstadoReporte.fromString normaliza a mayúsculas")
        void estadoReporteFromString_normaliza() {
            assertEquals(EstadoReporte.ACTIVE, EstadoReporte.fromString("active"));
            assertEquals(EstadoReporte.RESCUED, EstadoReporte.fromString("rescued"));
            assertEquals(EstadoReporte.DELETED, EstadoReporte.fromString("deleted"));
        }

        @Test
        @DisplayName("EstadoReporte.fromString con valor inválido lanza IllegalArgumentException")
        void estadoReporteFromString_invalido() {
            assertThrows(IllegalArgumentException.class,
                    () -> EstadoReporte.fromString("ELIMINADO"));
        }
    }
}
