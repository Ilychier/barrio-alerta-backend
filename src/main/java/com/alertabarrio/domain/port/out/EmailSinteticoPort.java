package com.alertabarrio.domain.port.out;

/**
 * Puerto de salida para generar un email sintético a partir de un teléfono.
 * <p>
 * El registro rápido (emergencia) no pide email: el usuario solo aporta su
 * celular. El dominio {@code User} exige un {@code Email} válido, así que el
 * adapter genera uno determinístico (ej. {@code 573001112233@mascotas.temp})
 * que el usuario podrá reemplazar después al completar su perfil.
 * <p>
 * Es un puerto (no un helper estático) para mantener el dominio puro y
 * permitir cambiar el formato sin tocar la lógica de negocio.
 */
public interface EmailSinteticoPort {

    /**
     * Genera un email sintético determinístico a partir de un teléfono.
     *
     * @param phone teléfono del usuario (ej. +573001112233)
     * @return email sintético (ej. 573001112233@mascotas.temp)
     */
    String generar(String phone);
}
