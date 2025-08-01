package gift.exception.member;

import org.springframework.http.HttpStatus;

public class MemberAlreadyExistsException extends MemberException {
    public MemberAlreadyExistsException(String message) {

        super(message, HttpStatus.CONFLICT);
    }
}
