package com.template.security.filter;


import com.template.model.user.wrapper.CustomUserDetails;
import com.template.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) return true; // CORS preflight

        String uri = request.getRequestURI();
        // TODO: Add other public endpoints if needed
        return uri.equals("/api/auth/login")
                || uri.equals("/api/auth/register")
                || uri.startsWith("/actuator/health")
                || uri.equals("/error")
                || uri.startsWith("/docs")
                || uri.startsWith("/swagger-ui")
                || uri.equals("/swagger-ui.html");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        // If already authenticated, skip
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            chain.doFilter(request, response);
            return;
        }

        // Get token from header
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);
        try {
            Claims claims = jwtService.validateToken(token);

            String sub = claims.getSubject();
            if (sub == null) {
                log.warn("Invalid token subject");
                chain.doFilter(request, response);
                return;
            }

            Long userId = Long.parseLong(sub);
            String username = claims.get("username", String.class);
            String role = claims.get("role", String.class);

            // TODO: Add other claims if needed
            //       remember to change the CustomUserDetails class and the JWT generation logic!
            CustomUserDetails cud = CustomUserDetails.fromClaims(userId, username, role);

            // Setting authentication in context
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(cud, null, cud.getAuthorities());
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(auth);

        } catch (io.jsonwebtoken.JwtException e) {
            SecurityContextHolder.clearContext();
            log.warn("JWT validation error: {}", e.getMessage());
        } catch (RuntimeException e) {
            SecurityContextHolder.clearContext();
            log.error("Internal auth error: {}", e.getMessage());
        }

        chain.doFilter(request, response);
    }
}