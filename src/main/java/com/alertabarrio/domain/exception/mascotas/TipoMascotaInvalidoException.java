package com.alertabarrio.domain.exception.mascotas;

import com.alertabarrio.domain.exception.CodigoError;
import com.alertabarrio.domain.exception.DomainException;

public class TipoMascotaInvalidoException extends DomainException {
    public TipoMascotaInvalidoException(String message) {
        super(message, CodigoError.VALIDACION);
    }
}
