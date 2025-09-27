package com.template.security.config;

import com.template.security.exception.handler.RestAccessDeniedHandler;
import com.template.security.exception.handler.RestAuthenticationEntryPoint;
import com.template.security.filter.JwtAuthenticationFilter;
import com.template.service.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.PermissionsPolicyHeaderWriter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

import static org.springframework.http.HttpMethod.POST;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtService jwtService;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final RestAccessDeniedHandler restAccessDeniedHandler;

    public SecurityConfig(JwtService jwtService,
                          RestAuthenticationEntryPoint restAuthenticationEntryPoint,
                          RestAccessDeniedHandler restAccessDeniedHandler) {
        this.jwtService = jwtService;
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
        this.restAccessDeniedHandler = restAccessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // === API stateless with JWT ===
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

                // === CORS ===
                .cors(cors -> {}) // Bean corsFilter() defined below

                // === Error handling JSON ===
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(restAuthenticationEntryPoint)
                        .accessDeniedHandler(restAccessDeniedHandler))

                // === Authorization ===
                //TODO: Here you can set public endpoints with .permitAll()
                //      remember to add them to the JWT filter whitelist too!
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(POST, "/api/auth/login").permitAll()
                        .requestMatchers(POST, "/api/auth/register").permitAll()
                        .requestMatchers("/actuator/health", "/actuator/health/**").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/docs", "/docs/**",
                                "/swagger-ui.html", "/swagger-ui/**").permitAll() // Only if using Swagger/OpenAPI
                        .anyRequest().authenticated()
                )

                // === Security Headers ===
                .headers(headers -> headers
                        .contentSecurityPolicy(csp -> csp.policyDirectives(String.join("; ",
                                "default-src 'self'",
                                "base-uri 'self'",
                                "frame-ancestors 'none'",
                                "img-src 'self' data:",
                                "script-src 'self'",
                                "style-src 'self' 'unsafe-inline'",
                                "font-src 'self' data:",
                                "connect-src 'self'"
                        )))
                        .contentTypeOptions(cto -> {})
                        .referrerPolicy(rp -> rp.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.NO_REFERRER))
                        .httpStrictTransportSecurity(hsts -> hsts
                                .includeSubDomains(true)
                                .maxAgeInSeconds(31536000))
                        .addHeaderWriter(new PermissionsPolicyHeaderWriter(
                                "geolocation=(), microphone=(), camera=()"
                        ))
                        .crossOriginOpenerPolicy(coop -> coop
                                .policy(org.springframework.security.web.header.writers
                                        .CrossOriginOpenerPolicyHeaderWriter.CrossOriginOpenerPolicy.SAME_ORIGIN))
                        .crossOriginResourcePolicy(corp -> corp
                                .policy(org.springframework.security.web.header.writers
                                        .CrossOriginResourcePolicyHeaderWriter.CrossOriginResourcePolicy.SAME_ORIGIN))
                );

        // === JWT Filter ===
        http.addFilterBefore(
                new JwtAuthenticationFilter(jwtService),
                UsernamePasswordAuthenticationFilter.class
        );

        return http.build();
    }

    // === CORS filter bean ===
    // TODO: This is a very permissive configuration, adjust it to your needs!
    @Bean
    public CorsFilter corsFilter() {

        CorsConfiguration cfg = new CorsConfiguration();

        cfg.setAllowCredentials(true);
        cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        cfg.addAllowedOriginPattern("*"); // TODO: Change with your front-end origin or remove if used with a mobile app
        cfg.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));
        cfg.setExposedHeaders(List.of("Authorization")); // Per il token JWT
        // Preflight cache
        cfg.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
        src.registerCorsConfiguration("/**", cfg);
        return new CorsFilter(src);
    }
}
