package com.volcan.api_gateway; 

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    
    private final String SECRET_KEY = "MiClaveSecretaSuperSeguraParaGasElVolcan2026";
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        
        if (path.contains("/api/usuarios/login") || 
            path.contains("/api/usuarios/registro") || 
            path.startsWith("/api/productos") || 
            path.contains("/v3/api-docs") || 
            path.contains("/swagger-ui")) {
            return chain.filter(exchange); 
        }

        
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete(); 
        }

        
        String token = authHeader.substring(7);
        
        try {
            
            Jwts.parser()
                .verifyWith(key)            
                .build()
                .parseSignedClaims(token);  
                
            return chain.filter(exchange); 
        } catch (Exception e) {
            
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete(); 
        }
    }

    @Override
    public int getOrder() {
        return -1; 
    }
}