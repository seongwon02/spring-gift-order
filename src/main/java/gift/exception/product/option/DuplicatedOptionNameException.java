package gift.exception.product.option;

import org.springframework.http.HttpStatus;

public class DuplicatedOptionNameException extends OptionException {
    public DuplicatedOptionNameException(String message) {

      super(message, HttpStatus.CONFLICT);
    }
}
