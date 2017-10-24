package ca.primat.shorturl.model.app;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Represents a REST error and is used to formulate a (HTTP) response
 */
public class RestError {
    @JsonIgnore
    private HttpStatus httpStatus;
    private int status;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> errors;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, List<String>> fieldErrors;

    public RestError(HttpStatus httpStatus, String message, Map<String, List<String>> fieldErrors) {
        this(httpStatus, message);
        this.fieldErrors = fieldErrors;
    }

    public RestError(HttpStatus httpStatus, String message, List<String> errors) {
        this(httpStatus, message);
        this.errors = errors;
    }

    public RestError(HttpStatus httpStatus, String message, String error) {
        this(httpStatus, message);
        this.errors = Collections.singletonList(error);
    }

    public RestError(HttpStatus httpStatus, String message) {
        super();
        this.httpStatus = httpStatus;
        this.status = httpStatus.value();
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        this.status = httpStatus.value();
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public Map<String, List<String>> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(Map<String, List<String>> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }
}
