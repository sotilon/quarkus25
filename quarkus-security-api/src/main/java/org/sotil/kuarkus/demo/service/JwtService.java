package org.sotil.kuarkus.demo.service;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.Duration;
import java.util.Set;

@ApplicationScoped
public class JwtService {

    public String generateToken(String username, String roles) {
        return Jwt.issuer("https://quarkus.io/issuer")
                .upn(username)
                .groups(Set.of(roles.split(","))) // Soporta múltiples roles separados por coma
                .expiresIn(Duration.ofHours(1)) // Expira en 1 hora
                .sign();
    }
}