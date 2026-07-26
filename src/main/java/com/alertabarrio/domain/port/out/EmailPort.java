package com.alertabarrio.domain.port.out;

import com.alertabarrio.domain.model.valueobject.Email;

public interface EmailPort {
    void enviar(Email destinatario, String asunto, String cuerpoHtml);
}
