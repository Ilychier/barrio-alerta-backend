package com.alertabarrio.infrastructure.security;

import com.alertabarrio.domain.port.out.PasswordEncoderPort;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class BcryptPasswordEncoderAdapter implements PasswordEncoderPort {

    @Override
    public String hashear(String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
    }

    @Override
    public boolean verificar(String rawPassword, String hash) {
        return BCrypt.checkpw(rawPassword, hash);
    }
}
