package com.template.security.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.time.OffsetDateTime;

/**
 * Manages 401 Unauthorized errors for REST APIs.
 * Sends a JSON response with Problem Details format.
 */
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper mapper;

    public RestAuthenticationEntryPoint(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Commences an authentication scheme.
     *
     * @param request  that resulted in an AuthenticationException
     * @param response so that the user agent can be advised of the failure
     * @param ex       that caused the invocation
     * @throws IOException in case of I/O errors
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException ex) throws IOException {

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "Unauthorized");
        pd.setTitle("Unauthorized");
        pd.setType(URI.create("about:blank")); // o URI della tua doc, es: https://api.tuodominio.com/errors/unauthorized
        pd.setProperty("timestamp", OffsetDateTime.now().toString());
        pd.setProperty("path", request.getRequestURI());
        // opzionale: codice applicativo
        pd.setProperty("errorCode", "AUTH_401");

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json; charset=UTF-8");
        mapper.writeValue(response.getWriter(), pd);
    }
}
