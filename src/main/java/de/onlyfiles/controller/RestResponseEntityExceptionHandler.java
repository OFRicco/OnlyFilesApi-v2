package de.onlyfiles.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import de.onlyfiles.exception.NoCurrentPrincipalException;
import de.onlyfiles.exception.UserAlreadyExistsException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NoCurrentPrincipalException.class)
    protected ResponseEntity<Object> noCurrentPrincipalException(RuntimeException ex, WebRequest request) {
        String callbackMessage = "Invalid session";
        
        return this.handleExceptionInternal(ex, callbackMessage, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
    
    @ExceptionHandler(UserAlreadyExistsException.class)
    protected ResponseEntity<Object> userAlreadyExistsException(RuntimeException ex, WebRequest request) {
        String callbackMessage = "User already exists";
        
        return this.handleExceptionInternal(ex, callbackMessage, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
}
