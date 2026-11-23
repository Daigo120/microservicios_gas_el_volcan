package com.volcan.usuarios.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {
    // Clave secreta (en producción debería ir en application.properties)
    private final String SECRET_KEY = "MiClaveSecretaSuperSeguraParaGasElVolcan2026";
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public String generarToken(Long id, String correo, String rol) {
        return Jwts.builder()
                .setSubject(correo)
                .claim("id", id)
                .claim("rol", rol)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 día de duración
                .signWith(key)
                .compact();
    }
}