package com.alertabarrio.domain.port.out;

/**
 * Puerto de salida para generar la clave temporal del registro rápido.
 * <p>
 * El usuario de emergencia no elige contraseña: el sistema genera una
 * temporal que se hashea y persiste. El usuario nunca la ve (auto-login);
 * solo se le pide definir una nueva en el primer acceso.
 */
public interface ClaveTemporalPort {

    /**
     * Genera una clave temporal legible (ej. 8 caracteres alfanuméricos).
     *
     * @return clave temporal en texto plano (se hashea antes de persistir)
     */
    String generar();
}
