package com.template.security.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.time.OffsetDateTime;

/**
 * Handles AccessDeniedException (HTTP 403) for REST APIs by returning a JSON response with Problem Details format.
 */
@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper mapper;

    public RestAccessDeniedHandler(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Handles an access denied failure.
     *
     * @param request  that resulted in an AccessDeniedException
     * @param response so that the user agent can be advised of the failure
     * @param ex       that caused the invocation
     * @throws IOException in case of I/O errors
     */
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException ex) throws IOException {

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, "Forbidden");
        pd.setTitle("Forbidden");
        pd.setType(URI.create("about:blank")); // or doc URL
        pd.setProperty("timestamp", OffsetDateTime.now().toString());
        pd.setProperty("path", request.getRequestURI());
        pd.setProperty("errorCode", "AUTH_403");

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json; charset=UTF-8");
        mapper.writeValue(response.getWriter(), pd);
    }
}
