package com.alertabarrio.domain.port.out;

import com.alertabarrio.domain.model.valueobject.Email;

public interface TokenServicePort {
    String generarToken(Email email);
    Email validarToken(String token);
}
