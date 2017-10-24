package ca.primat.shorturl.exception.rest;

import ca.primat.shorturl.model.app.RestError;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Global exception handler
 */
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    // Handles REST HTTP 400 (invalid data)
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        List<String> errors = new ArrayList<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }

        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        RestError restError = new RestError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return handleExceptionInternal(ex, restError, headers, restError.getHttpStatus(), request);
    }

    // Handles REST HTTP 400 (unreadable data)
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        RestError restError = new RestError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage());
        return handleExceptionInternal(ex, restError, headers, restError.getHttpStatus(), request);
    }

    // Handles REST HTTP 404
    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<Object> handleNotFound(
            NotFoundException ex,
            WebRequest request) {

        RestError restError = new RestError(HttpStatus.NOT_FOUND, ex.getLocalizedMessage());
        return handleExceptionInternal(ex, restError, new HttpHeaders(), restError.getHttpStatus(), request);
    }

    // Handles violations of storage constraints
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolation(
            DataIntegrityViolationException ex,
            WebRequest request) {

        Map<String, List<String>> fieldErrors = new HashMap<>();
        String detail = ex.getMostSpecificCause().getLocalizedMessage();
        String message = "A data integrity constraint violation has occurred";

        if (detail.contains("UNIQUE_URL_ON_SHORT_URL")) {
            List<String> list = new ArrayList<>();
            list.add("A short URL already exists for the provided URL.");
            fieldErrors.put("url", list);
        }

        RestError restError = new RestError(HttpStatus.INTERNAL_SERVER_ERROR, message, fieldErrors);
        return handleExceptionInternal(ex, restError, new HttpHeaders(), restError.getHttpStatus(), request);
    }

    // Handles server errors that don't fall under any particular category
    @ExceptionHandler(InternalServerErrorException.class)
    protected ResponseEntity<Object> handleInternalServerError(InternalServerErrorException ex, WebRequest request) {
        RestError restError = new RestError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage());
        return handleExceptionInternal(ex, restError, new HttpHeaders(), restError.getHttpStatus(), request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleError(Exception ex, WebRequest request)   {
        RestError restError = new RestError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage());
        return handleExceptionInternal(ex, restError, new HttpHeaders(), restError.getHttpStatus(), request);
    }
}
