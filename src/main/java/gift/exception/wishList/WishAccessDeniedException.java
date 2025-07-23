package gift.exception.wishList;

import org.springframework.http.HttpStatus;

public class WishAccessDeniedException extends WishException {
    public WishAccessDeniedException(String message) {

        super(message, HttpStatus.FORBIDDEN);
    }
}
