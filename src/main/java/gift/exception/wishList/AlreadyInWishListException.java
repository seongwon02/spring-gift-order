package gift.exception.wishList;

import org.springframework.http.HttpStatus;

public class AlreadyInWishListException extends WishException {
    public AlreadyInWishListException(String message) {

        super(message, HttpStatus.CONFLICT);
    }
}
