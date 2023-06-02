package de.onlyfiles.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import de.onlyfiles.exception.DeleteFailedException;
import de.onlyfiles.exception.FileNotFoundException;
import de.onlyfiles.exception.GroupNotFoundException;
import de.onlyfiles.exception.IdIsNotEqualWithUsernameException;
import de.onlyfiles.exception.InsufficientPermissionException;
import de.onlyfiles.exception.MySQLException;
import de.onlyfiles.exception.NoCurrentPrincipalException;
import de.onlyfiles.exception.ObjectContainsException;
import de.onlyfiles.exception.UserNotFoundException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NoCurrentPrincipalException.class)
    protected ResponseEntity<Object> noCurrentPrincipalException(RuntimeException ex, WebRequest request) {
        String callbackMessage = "Invalid session";
        
        return this.handleExceptionInternal(ex, callbackMessage, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(ObjectContainsException.class)
    protected ResponseEntity<Object> objectAlreadyExistsException(RuntimeException ex, WebRequest request) {
        String callbackMessage = "Contains object already";
        
        return this.handleExceptionInternal(ex, callbackMessage, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
    
    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<Object> userNotFoundException(RuntimeException ex, WebRequest request) {
        String callbackMessage = "User not found";
        
        return this.handleExceptionInternal(ex, callbackMessage, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(GroupNotFoundException.class)
    protected ResponseEntity<Object> groupNotFoundException(RuntimeException ex, WebRequest request) {
        String callbackMessage = "Group not found";
        
        return this.handleExceptionInternal(ex, callbackMessage, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(FileNotFoundException.class)
    protected ResponseEntity<Object> fileNotFoundException(RuntimeException ex, WebRequest request) {
        String callbackMessage = "File not found";
        
        return this.handleExceptionInternal(ex, callbackMessage, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(DeleteFailedException.class)
    protected ResponseEntity<Object> deleteFailedException(RuntimeException ex, WebRequest request) {
        String callbackMessage = "Couldn't delete from database";
        
        return this.handleExceptionInternal(ex, callbackMessage, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(IdIsNotEqualWithUsernameException.class)
    protected ResponseEntity<Object> idIsNotEqualWithUserException(RuntimeException ex, WebRequest request) {
        String callbackMessage = "Request id is not equal with the user founded name";
        
        return this.handleExceptionInternal(ex, callbackMessage, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(InsufficientPermissionException.class)
    protected ResponseEntity<Object> insufficientPermissionException(RuntimeException ex, WebRequest request) {
        String callbackMessage = "Insufficient Permission";
        
        return this.handleExceptionInternal(ex, callbackMessage, new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }
    
    @ExceptionHandler(MySQLException.class)
    protected ResponseEntity<Object> mySQLException(RuntimeException ex, WebRequest request) {
        String callbackMessage = "There were a error with calling MySQL. Please try again or contact an Admin.";
        
        return this.handleExceptionInternal(ex, callbackMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
