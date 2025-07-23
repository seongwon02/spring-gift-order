package gift.exception.product.option;

import org.springframework.http.HttpStatus;

public class SoldOutException extends OptionException {
    public SoldOutException(String message) {

        super(message, HttpStatus.CONFLICT);
    }
}
