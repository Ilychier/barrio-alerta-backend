package com.alertabarrio.application.command;

public record ParchearUsuarioCommand(Long id, String name, String email, String phone, String address, String password, Long barrioId) {
}
