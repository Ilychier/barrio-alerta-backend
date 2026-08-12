package com.alertabarrio.domain.model;

/**
 * Read model de ciudad (no es un aggregate completo).
 * Se usa para exponer el nombre y país de la ciudad en la sesión.
 */
public record CiudadInfo(Long id, String nombre, String departamento, String pais) {
}
