package com.alertabarrio.infrastructure.storage;

import com.alertabarrio.domain.exception.ArchivoInvalidoException;
import com.alertabarrio.domain.port.out.AlmacenamientoArchivoPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Adapter de almacenamiento en disco local (KISS/YAGNI para un solo VPS).
 * <p>
 * - Valida el tipo MIME real por bytes mágicos (no por extensión ni por
 *   el content-type declarado, que es falsificable).
 * - Acepta cualquier imagen: JPEG, PNG, WebP, GIF, HEIC/HEIF (en emergencia
 *   no hay tiempo de convertir formato).
 * - Límite: 8 MB por archivo.
 * - Nombre UUID no predecible: evita sobrescrituras y enumeración.
 * <p>
 * Los bytes se sirven por Nginx (location /uploads/) en producción; en dev
 * el backend puede servirlos como estáticos. Este adapter solo persiste.
 */
@Component
public class LocalDiskAlmacenamientoAdapter implements AlmacenamientoArchivoPort {

    public static final long MAX_BYTES = 8L * 1024 * 1024; // 8 MB

    private static final Set<String> MIMES_PERMITIDOS = Set.of(
            "image/jpeg", "image/png", "image/webp", "image/gif",
            "image/heic", "image/heif"
    );

    /** MIME → extensión de archivo. */
    private static final Map<String, String> EXTENSIONES = Map.of(
            "image/jpeg", "jpg",
            "image/png", "png",
            "image/webp", "webp",
            "image/gif", "gif",
            "image/heic", "heic",
            "image/heif", "heif"
    );

    private final Path directorio;

    public LocalDiskAlmacenamientoAdapter(@Value("${app.uploads.dir:uploads}") String directorio) {
        this.directorio = Path.of(directorio);
    }

    @Override
    public String guardarImagen(byte[] contenido, String mimeType) {
        validarMime(mimeType);
        validarTamanio(contenido);
        validarBytesMagicos(contenido, mimeType);

        String nombre = UUID.randomUUID() + "." + EXTENSIONES.get(mimeType);
        try {
            Files.createDirectories(directorio);
            Files.write(directorio.resolve(nombre), contenido);
        } catch (IOException e) {
            throw new ArchivoInvalidoException("No se pudo guardar la imagen: " + e.getMessage());
        }
        return "/uploads/" + nombre;
    }

    private void validarMime(String mimeType) {
        if (mimeType == null || !MIMES_PERMITIDOS.contains(mimeType)) {
            throw new ArchivoInvalidoException(
                    "Formato no permitido. Solo se aceptan imágenes (JPEG, PNG, WebP, GIF, HEIC/HEIF)");
        }
    }

    private void validarTamanio(byte[] contenido) {
        if (contenido == null || contenido.length == 0) {
            throw new ArchivoInvalidoException("El archivo está vacío");
        }
        if (contenido.length > MAX_BYTES) {
            throw new ArchivoInvalidoException("La imagen no puede superar 8 MB");
        }
    }

    /**
     * Valida la firma real del archivo contra el MIME declarado.
     * Un .jpg renombrado (ej. ejecutable MZ) se rechaza aquí.
     */
    private void validarBytesMagicos(byte[] bytes, String mimeType) {
        boolean coincide = switch (mimeType) {
            case "image/jpeg" -> esJpeg(bytes);
            case "image/png" -> esPng(bytes);
            case "image/webp" -> esWebp(bytes);
            case "image/gif" -> esGif(bytes);
            case "image/heic", "image/heif" -> esHeic(bytes);
            default -> false;
        };
        if (!coincide) {
            throw new ArchivoInvalidoException("El contenido del archivo no coincide con el formato declarado");
        }
    }

    private boolean esJpeg(byte[] b) {
        return b.length >= 3 && (b[0] & 0xFF) == 0xFF && (b[1] & 0xFF) == 0xD8 && (b[2] & 0xFF) == 0xFF;
    }

    private boolean esPng(byte[] b) {
        byte[] firma = {(byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A};
        if (b.length < firma.length) return false;
        for (int i = 0; i < firma.length; i++) {
            if (b[i] != firma[i]) return false;
        }
        return true;
    }

    private boolean esWebp(byte[] b) {
        return b.length >= 12
                && b[0] == 'R' && b[1] == 'I' && b[2] == 'F' && b[3] == 'F'
                && b[8] == 'W' && b[9] == 'E' && b[10] == 'B' && b[11] == 'P';
    }

    private boolean esGif(byte[] b) {
        if (b.length < 6) return false;
        String header = new String(b, 0, 6, java.nio.charset.StandardCharsets.US_ASCII);
        return header.equals("GIF87a") || header.equals("GIF89a");
    }

    /**
     * HEIC/HEIF son contenedores ISO BMFF: bytes 4-7 = "ftyp", bytes 8-11 = brand.
     * Brands válidos: heic, heix, hevc, hevx, mif1, msf1.
     */
    private boolean esHeic(byte[] b) {
        if (b.length < 12) return false;
        if (b[4] != 'f' || b[5] != 't' || b[6] != 'y' || b[7] != 'p') return false;
        String brand = new String(b, 8, 4, java.nio.charset.StandardCharsets.US_ASCII);
        return Set.of("heic", "heix", "hevc", "hevx", "mif1", "msf1").contains(brand);
    }
}
