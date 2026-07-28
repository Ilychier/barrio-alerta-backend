package com.alertabarrio.application.command;

public record RegistrarUsuarioCommand(String name, String email, String phone, String address, String password, Long barrioId) {
}
