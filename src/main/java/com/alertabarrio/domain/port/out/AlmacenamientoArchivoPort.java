package com.alertabarrio.domain.port.out;

/**
 * Puerto de salida para almacenar imágenes subidas por usuarios.
 * <p>
 * El dominio/application NO conoce dónde se guardan los bytes (disco local,
 * S3, MinIO...). Solo recibe la URL pública resultante, que se persiste
 * como {@code fotoUrl} en el reporte.
 * <p>
 * La validación (tamaño, MIME real por bytes mágicos) es responsabilidad
 * del adapter que implementa este puerto.
 */
public interface AlmacenamientoArchivoPort {

    /**
     * Guarda una imagen y retorna su URL pública (ej. {@code /uploads/{uuid}.jpg}).
     *
     * @param contenido bytes de la imagen
     * @param mimeType  tipo MIME declarado por el cliente (ej. image/jpeg, image/heic)
     * @return URL pública relativa para servir el archivo
     * @throws com.alertabarrio.domain.exception.ArchivoInvalidoException si el archivo
     *         no es una imagen permitida, excede el tamaño máximo o el MIME no coincide
     *         con el contenido real
     */
    String guardarImagen(byte[] contenido, String mimeType);
}
