package com.alertabarrio.domain.port.out;

public interface PasswordEncoderPort {
    String hashear(String rawPassword);
    boolean verificar(String rawPassword, String hash);
}
