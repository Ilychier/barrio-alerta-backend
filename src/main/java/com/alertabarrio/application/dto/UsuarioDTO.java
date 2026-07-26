package com.alertabarrio.application.dto;

public record UsuarioDTO(Long id, String name, String email, String phone, String address, Long barrioId) {
}
