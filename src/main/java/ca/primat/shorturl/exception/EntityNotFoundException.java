package ca.primat.shorturl.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Entity not found")
public class EntityNotFoundException extends RuntimeException {
    EntityNotFoundException() {
        super("Entity not found");
    }

    EntityNotFoundException(String message) {
        super(message);
    }
}
