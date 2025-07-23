package gift.exception.member;

import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends MemberException{

    public InvalidCredentialsException(String message) {

        super(message, HttpStatus.UNAUTHORIZED);
    }

}
