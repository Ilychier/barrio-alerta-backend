package com.alertabarrio.application.command;

/**
 * Command para cambiar la contraseña.
 * <p>
 * {@code passwordActual} es null cuando el usuario tiene clave temporal
 * (el JWT ya lo autentica; no conoce la temporal porque nunca la vio).
 */
public record CambiarPasswordCommand(
        String identificador,   // email o phone (login por cualquiera de los dos)
        String passwordActual,  // null si passwordTemporal=true
        String passwordNueva
) {
}
