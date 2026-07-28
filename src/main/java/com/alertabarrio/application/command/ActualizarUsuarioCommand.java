package com.alertabarrio.application.command;

public record ActualizarUsuarioCommand(Long id, String name, String email, String phone, String address, String password, Long barrioId) {
}
