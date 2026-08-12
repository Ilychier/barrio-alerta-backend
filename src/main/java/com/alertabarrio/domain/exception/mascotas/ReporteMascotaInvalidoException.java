package com.alertabarrio.domain.exception.mascotas;

import com.alertabarrio.domain.exception.CodigoError;
import com.alertabarrio.domain.exception.DomainException;

public class ReporteMascotaInvalidoException extends DomainException {
    public ReporteMascotaInvalidoException(String message) {
        super(message, CodigoError.VALIDACION);
    }
}
