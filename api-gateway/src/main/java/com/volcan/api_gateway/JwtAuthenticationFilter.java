package com.volcan.api_gateway;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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
        HttpMethod method = exchange.getRequest().getMethod();

        
        if (HttpMethod.OPTIONS.equals(method)
                || path.equals("/api/usuarios/login")
                || path.equals("/api/usuarios/registro")
                || path.contains("/v3/api-docs")
                || path.contains("/swagger-ui")
                || isPublicProductRead(path, method)) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return reject(exchange, HttpStatus.UNAUTHORIZED);
        }

        try {
            Claims claims = Jwts.parser().verifyWith(key).build()
                    .parseSignedClaims(authHeader.substring(7)).getPayload();
            String role = claims.get("rol", String.class);
            boolean adminOnly = isAdminOnlyOperation(path, method);
            if (adminOnly && (role == null || !"ADMIN".equalsIgnoreCase(role))) {
                return reject(exchange, HttpStatus.FORBIDDEN);
            }
            return chain.filter(exchange);
        } catch (Exception e) {
            return reject(exchange, HttpStatus.UNAUTHORIZED);
        }
    }

  
   
    private boolean isPublicProductRead(String path, HttpMethod method) {
        if (!HttpMethod.GET.equals(method)) {
            return false;
        }
        return path.equals("/api/productos") || path.matches("/api/productos/[^/]+");
    }

    private boolean isAdminOnlyOperation(String path, HttpMethod method) {
        
        if (path.equals("/api/usuarios") || path.matches("/api/usuarios/[^/]+")) {
            return HttpMethod.GET.equals(method)
                    || HttpMethod.PUT.equals(method)      
                    || HttpMethod.PATCH.equals(method)   
                    || HttpMethod.DELETE.equals(method);
        }
        
        if (path.equals("/api/productos") || path.startsWith("/api/productos/")) {
            return HttpMethod.POST.equals(method)
                    || HttpMethod.PUT.equals(method)
                    || HttpMethod.PATCH.equals(method)
                    || HttpMethod.DELETE.equals(method);
        }
        return false;
    }

    private Mono<Void> reject(ServerWebExchange exchange, HttpStatus status) {
        exchange.getResponse().setStatusCode(status);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() { return -1; }
}