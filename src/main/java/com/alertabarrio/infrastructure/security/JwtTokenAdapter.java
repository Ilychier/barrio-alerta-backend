package com.alertabarrio.infrastructure.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.alertabarrio.domain.exception.TokenInvalidoException;
import com.alertabarrio.domain.model.valueobject.Email;
import com.alertabarrio.domain.port.out.TokenServicePort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenAdapter implements TokenServicePort {

    private final String secret;
    private final long expiration;

    public JwtTokenAdapter(
            @Value("${jwt.secret:default-secret-key-super-secure-change-me}") String secret,
            @Value("${jwt.expiration:86400000}") long expiration) {
        this.secret = secret;
        this.expiration = expiration;
    }

    @Override
    public String generarToken(Email email) {
        return JWT.create()
                .withSubject(email.value())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expiration))
                .sign(Algorithm.HMAC256(secret));
    }

    @Override
    public Email validarToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return new Email(jwt.getSubject());
        } catch (Exception e) {
            throw new TokenInvalidoException("Token inválido o expirado: " + e.getMessage());
        }
    }
}
