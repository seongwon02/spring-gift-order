package gift.exception.member;

import org.springframework.http.HttpStatus;

public class MemberNotFoundException extends MemberException {
    public MemberNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
