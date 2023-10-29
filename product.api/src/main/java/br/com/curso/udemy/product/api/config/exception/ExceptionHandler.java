package br.com.curso.udemy.product.api.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleValidationException(ValidationException validationException){
        var details = new ExceptionDetails();
        details.setStatus(HttpStatus.BAD_REQUEST.value());
        details.setMessage(validationException.getMessage());

        return new ResponseEntity<>(details, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthenticationException(AuthenticationException authenticationException){
        var details = new ExceptionDetails();
        details.setStatus(HttpStatus.UNAUTHORIZED.value());
        details.setMessage(authenticationException.getMessage());

        return new ResponseEntity<>(details, HttpStatus.UNAUTHORIZED);
    }
}
