package com.alertabarrio.infrastructure.storage;

import com.alertabarrio.domain.exception.ArchivoInvalidoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("LocalDiskAlmacenamientoAdapter (infraestructura)")
class LocalDiskAlmacenamientoAdapterTest {

    @TempDir
    Path tempDir;

    private LocalDiskAlmacenamientoAdapter adapter() {
        return new LocalDiskAlmacenamientoAdapter(tempDir.toString());
    }

    private byte[] jpegBytes() {
        // Firma JPEG: FF D8 FF E0 ... (JFIF)
        byte[] bytes = new byte[16];
        bytes[0] = (byte) 0xFF;
        bytes[1] = (byte) 0xD8;
        bytes[2] = (byte) 0xFF;
        bytes[3] = (byte) 0xE0;
        return bytes;
    }

    private byte[] pngBytes() {
        // Firma PNG: 89 50 4E 47 0D 0A 1A 0A
        return new byte[]{
                (byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A,
                0x00, 0x00, 0x00, 0x0D, 0x49, 0x48, 0x44, 0x52
        };
    }

    private byte[] heicBytes() {
        // ISO BMFF: tamaño + "ftyp" + brand "heic"
        byte[] bytes = new byte[16];
        bytes[4] = 'f'; bytes[5] = 't'; bytes[6] = 'y'; bytes[7] = 'p';
        bytes[8] = 'h'; bytes[9] = 'e'; bytes[10] = 'i'; bytes[11] = 'c';
        return bytes;
    }

    private byte[] webpBytes() {
        // RIFF....WEBP
        byte[] bytes = new byte[16];
        bytes[0] = 'R'; bytes[1] = 'I'; bytes[2] = 'F'; bytes[3] = 'F';
        bytes[8] = 'W'; bytes[9] = 'E'; bytes[10] = 'B'; bytes[11] = 'P';
        return bytes;
    }

    private byte[] gifBytes() {
        return "GIF89a".getBytes();
    }

    private byte[] exeBytes() {
        // MZ header (ejecutable Windows)
        return new byte[]{'M', 'Z', 0x00, 0x01, 0x02, 0x03};
    }

    @Nested
    @DisplayName("guardarImagen()")
    class GuardarImagen {

        @Test
        @DisplayName("con JPEG válido guarda archivo y retorna URL /uploads/{uuid}.jpg")
        void conJpegValido_guardaYRetornaUrl() throws IOException {
            String url = adapter().guardarImagen(jpegBytes(), "image/jpeg");

            assertTrue(url.startsWith("/uploads/"));
            assertTrue(url.endsWith(".jpg"));
            String nombre = url.substring("/uploads/".length());
            assertTrue(Files.exists(tempDir.resolve(nombre)));
            assertArrayEquals(jpegBytes(), Files.readAllBytes(tempDir.resolve(nombre)));
        }

        @Test
        @DisplayName("con PNG válido retorna URL con extensión .png")
        void conPngValido_retornaPng() {
            String url = adapter().guardarImagen(pngBytes(), "image/png");
            assertTrue(url.endsWith(".png"));
        }

        @Test
        @DisplayName("con HEIC válido retorna URL con extensión .heic")
        void conHeicValido_retornaHeic() {
            String url = adapter().guardarImagen(heicBytes(), "image/heic");
            assertTrue(url.endsWith(".heic"));
        }

        @Test
        @DisplayName("con WebP válido retorna URL con extensión .webp")
        void conWebpValido_retornaWebp() {
            String url = adapter().guardarImagen(webpBytes(), "image/webp");
            assertTrue(url.endsWith(".webp"));
        }

        @Test
        @DisplayName("con GIF válido retorna URL con extensión .gif")
        void conGifValido_retornaGif() {
            String url = adapter().guardarImagen(gifBytes(), "image/gif");
            assertTrue(url.endsWith(".gif"));
        }

        @Test
        @DisplayName("con MIME no permitido lanza ArchivoInvalidoException")
        void conMimeNoPermitido_lanzaExcepcion() {
            assertThrows(ArchivoInvalidoException.class,
                    () -> adapter().guardarImagen(jpegBytes(), "application/pdf"));
        }

        @Test
        @DisplayName("con MIME null lanza ArchivoInvalidoException")
        void conMimeNull_lanzaExcepcion() {
            assertThrows(ArchivoInvalidoException.class,
                    () -> adapter().guardarImagen(jpegBytes(), null));
        }

        @Test
        @DisplayName("con bytes que no coinciden con el MIME declarado lanza ArchivoInvalidoException")
        void conBytesInconsistentes_lanzaExcepcion() {
            // Declara JPEG pero envía bytes de un ejecutable MZ
            assertThrows(ArchivoInvalidoException.class,
                    () -> adapter().guardarImagen(exeBytes(), "image/jpeg"));
        }

        @Test
        @DisplayName("con archivo mayor a 8 MB lanza ArchivoInvalidoException")
        void conArchivoMayorA8MB_lanzaExcepcion() {
            byte[] grande = new byte[8 * 1024 * 1024 + 1];
            System.arraycopy(jpegBytes(), 0, grande, 0, 3);
            assertThrows(ArchivoInvalidoException.class,
                    () -> adapter().guardarImagen(grande, "image/jpeg"));
        }

        @Test
        @DisplayName("con contenido vacío lanza ArchivoInvalidoException")
        void conContenidoVacio_lanzaExcepcion() {
            assertThrows(ArchivoInvalidoException.class,
                    () -> adapter().guardarImagen(new byte[0], "image/jpeg"));
        }

        @Test
        @DisplayName("genera nombres únicos (UUID) para archivos distintos")
        void generaNombresUnicos() {
            String url1 = adapter().guardarImagen(jpegBytes(), "image/jpeg");
            String url2 = adapter().guardarImagen(jpegBytes(), "image/jpeg");
            assertNotEquals(url1, url2);
        }
    }
}
