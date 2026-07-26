package com.alertabarrio.infrastructure.security;

import com.alertabarrio.domain.port.out.TokenServicePort;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final TokenServicePort tokenService;

    public AuthInterceptor(TokenServicePort tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                String email = tokenService.validarToken(token).value();
                request.setAttribute("currentUserEmail", email);
                return true;
            } catch (Exception e) {
                // Token inválido
            }
        }

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.getWriter().write("{\"errors\": [{\"name\": \"Unauthorized: Missing or invalid token\"}]}");
        return false;
    }
}
