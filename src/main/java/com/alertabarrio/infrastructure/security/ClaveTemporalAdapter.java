package com.alertabarrio.infrastructure.security;

import com.alertabarrio.domain.port.out.ClaveTemporalPort;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

/**
 * Adapter del puerto {@link ClaveTemporalPort}.
 * <p>
 * Genera una clave de 8 caracteres alfanuméricos (A-Z, 0-9) con
 * {@link SecureRandom}. Suficiente para un acceso único de emergencia:
 * el usuario la reemplaza en el primer login (flag passwordTemporal).
 */
@Component
public class ClaveTemporalAdapter implements ClaveTemporalPort {

    private static final String ALFABETO = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final int LONGITUD = 8;
    private final SecureRandom random = new SecureRandom();

    @Override
    public String generar() {
        StringBuilder sb = new StringBuilder(LONGITUD);
        for (int i = 0; i < LONGITUD; i++) {
            sb.append(ALFABETO.charAt(random.nextInt(ALFABETO.length())));
        }
        return sb.toString();
    }
}
