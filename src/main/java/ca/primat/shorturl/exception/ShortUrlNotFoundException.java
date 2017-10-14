package ca.primat.shorturl.exception;

public class ShortUrlNotFoundException extends EntityNotFoundException {

    public ShortUrlNotFoundException() {
        super("ShortUrl not found");
    }

    public ShortUrlNotFoundException(String message) {
        super(message);
    }
}
