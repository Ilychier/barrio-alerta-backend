package com.alertabarrio.domain.exception.mascotas;

import com.alertabarrio.domain.exception.CodigoError;
import com.alertabarrio.domain.exception.DomainException;

public class CiudadInvalidaException extends DomainException {
    public CiudadInvalidaException(String message) {
        super(message, CodigoError.VALIDACION);
    }
}
