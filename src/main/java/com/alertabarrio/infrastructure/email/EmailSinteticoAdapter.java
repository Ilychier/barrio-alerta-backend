package com.alertabarrio.infrastructure.email;

import com.alertabarrio.domain.port.out.EmailSinteticoPort;
import org.springframework.stereotype.Component;

/**
 * Adapter del puerto {@link EmailSinteticoPort}.
 * <p>
 * Formato: {@code {phone sin '+'}@mascotas.temp} — determinístico, reversible
 * y válido para el VO {@code Email} del dominio. El usuario lo reemplaza al
 * completar su perfil (PUT /api/usuarios/{id}).
 */
@Component
public class EmailSinteticoAdapter implements EmailSinteticoPort {

    private static final String DOMINIO = "@mascotas.temp";

    @Override
    public String generar(String phone) {
        String limpio = phone.replace("+", "").replaceAll("[^0-9]", "");
        return limpio + DOMINIO;
    }
}
