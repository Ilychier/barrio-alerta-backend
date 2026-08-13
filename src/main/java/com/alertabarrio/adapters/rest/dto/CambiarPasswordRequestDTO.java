package com.alertabarrio.adapters.rest.dto;

/**
 * Request para cambiar la contraseña.
 * <p>
 * {@code passwordActual} es null cuando el usuario tiene clave temporal
 * (el JWT ya lo autentica; nunca vio la temporal).
 */
public record CambiarPasswordRequestDTO(
        String identificador,   // email o phone
        String passwordActual,  // null si passwordTemporal=true
        String passwordNueva
) {
}
