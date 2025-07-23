package gift.exception.product;

import org.springframework.http.HttpStatus;

public class ProductNotFoundException extends ProductException{
    public ProductNotFoundException(String message) {

        super(message, HttpStatus.NOT_FOUND);
    }
}
