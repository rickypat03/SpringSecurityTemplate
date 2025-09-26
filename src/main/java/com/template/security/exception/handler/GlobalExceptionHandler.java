package com.template.security.exception.handler;


import com.template.security.exception.custom.InvalidTokenException;
import com.template.security.exception.custom.JwtAuthenticationException;
import com.template.security.exception.custom.ResourceAlreadyExistsException;
import com.template.security.exception.custom.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private ProblemDetail base(HttpStatus status, String title, String detail, String errorCode) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, detail);
        pd.setTitle(title);
        pd.setType(URI.create("about:blank")); // o pagina di documentazione errori
        pd.setProperty("timestamp", OffsetDateTime.now().toString());
        pd.setProperty("errorCode", errorCode);
        return pd;
    }

    /**
     * Aggiunge il percorso della richiesta al ProblemDetail.
     * @param pd Il ProblemDetail da modificare.
     * @param path Il percorso della richiesta.
     * @return Il ProblemDetail aggiornato con il percorso.
     */
    private ProblemDetail withPath(ProblemDetail pd, String path) {
        pd.setProperty("path", path);
        return pd;
    }

    // 400 - Validazione Bean Validationd
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));

        log.warn("Validation error: {}", errors);
        ProblemDetail pd = base(HttpStatus.BAD_REQUEST, "Validation failed", "One or more fields are invalid", "VAL_400");
        pd.setProperty("errors", errors);
        return pd;
    }

    // 403 - Autorizzazione negata
    @ExceptionHandler(AuthorizationDeniedException.class)
    public ProblemDetail handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
        log.warn("Authorization denied: {}", ex.getMessage());
        return base(HttpStatus.FORBIDDEN, "Access denied", "You do not have permission to access this resource", "AUTH_403_DENIED");
    }

    // 409 - Conflitto di versione (optimistic locking)
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ProblemDetail handleOptimisticLockingFailure(ObjectOptimisticLockingFailureException ex) {
        log.error("Optimistic locking failure: {}", ex.getMessage(), ex);
        return base(HttpStatus.CONFLICT, "Conflict", "The resource was modified by another transaction. Please retry.", "DATA_409_OPTIMISTIC_LOCK");
    }

    // 400 - JSON malformato / payload non leggibile
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleParsingException(HttpMessageNotReadableException ex) {
        log.warn("Parsing error: {}", ex.getMessage());
        return base(HttpStatus.BAD_REQUEST, "Malformed JSON request", "Body could not be parsed", "JSON_400");
    }

    // 404 - Risorsa non trovata
    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.info("Resource not found: {}", ex.getMessage());
        return base(HttpStatus.NOT_FOUND, "Resource not found", "The requested resource does not exist", "RES_404");
    }

    // 409 - Conflitto (già esiste)
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ProblemDetail handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex) {
        log.info("Resource already exists: {}", ex.getMessage());
        return base(HttpStatus.CONFLICT, "Resource already exists", "A resource with the same identifier already exists", "RES_409");
    }

    // 409 - Violazione integrità dati
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail onDataIntegrity(DataIntegrityViolationException ex) {
        log.error("Data integrity violation: {}", ex.getMessage());
        return base(HttpStatus.CONFLICT, "Data integrity violation", "A data integrity error occurred", "DATA_409_INTEGRITY");
    }

    // 401 - Credenziali errate
    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail handleBadCredentialsException(BadCredentialsException ex) {
        log.info("Bad credentials: {}", ex.getMessage());
        return base(HttpStatus.UNAUTHORIZED, "Invalid username or password", "Unauthorized", "AUTH_401_BAD_CREDENTIALS");
    }

    // 401 - Token invalido
    @ExceptionHandler(InvalidTokenException.class)
    public ProblemDetail handleInvalidTokenException(InvalidTokenException ex) {
        log.info("Invalid token: {}", ex.getMessage());
        return base(HttpStatus.UNAUTHORIZED, "Invalid token", "Unauthorized", "AUTH_401_INVALID_TOKEN");
    }

    // 401 - JWT auth error generico
    @ExceptionHandler(JwtAuthenticationException.class)
    public ProblemDetail handleJwtAuthenticationException(JwtAuthenticationException ex) {
        log.info("JWT authentication error: {}", ex.getMessage());
        return base(HttpStatus.UNAUTHORIZED, "Unauthorized", "JWT authentication failed", "AUTH_401_JWT");
    }

    // 400 - Argomenti non validi
    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Illegal argument: {}", ex.getMessage(), ex);
        return base(HttpStatus.BAD_REQUEST, "Bad request", ex.getMessage(), "GEN_400_ILLARG");
    }

    // 500 - Stati illegali
    @ExceptionHandler(IllegalStateException.class)
    public ProblemDetail handleIllegalStateException(IllegalStateException ex) {
        log.error("Illegal state: {}", ex.getMessage(), ex);
        return base(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", "An unexpected error occurred", "GEN_500_ILLSTATE");
    }

    // 500 - Runtime
    @ExceptionHandler(RuntimeException.class)
    public ProblemDetail handleRuntimeException(RuntimeException ex) {
        log.error("Runtime error: {}", ex.getMessage(), ex);
        return base(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", "An unexpected error occurred", "GEN_500_RUNTIME");
    }

    // 500 - Fallback
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception ex) {
        log.error("Generic error: {}", ex.getMessage(), ex);
        return base(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", "An unexpected error occurred", "GEN_500");
    }
}
