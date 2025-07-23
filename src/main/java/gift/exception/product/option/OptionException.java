package gift.exception.product.option;

import org.springframework.http.HttpStatus;

public class OptionException extends RuntimeException {

    private final HttpStatus httpStatus;

    public OptionException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
