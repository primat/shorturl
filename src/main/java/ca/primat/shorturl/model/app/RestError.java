package ca.primat.shorturl.model.app;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

/**
 * Represents a REST error and is used to formulate a (HTTP) response
 */
public class RestError {
    @JsonIgnore
    private HttpStatus httpStatus;
    private int status;
    private String message;
    private List<String> errors;

    public RestError(HttpStatus httpStatus, String message, List<String> errors) {
        super();
        this.httpStatus = httpStatus;
        this.status = httpStatus.value(); // http status <i>code</i>
        this.message = message;
        this.errors = errors;
    }

    public RestError(HttpStatus httpStatus, String message, String error) {
        super();
        this.httpStatus = httpStatus;
        this.status = httpStatus.value();
        this.message = message;
        errors = Collections.singletonList(error);
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        this.status = httpStatus.value();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public int getStatus() {
        return status;
    }
}
