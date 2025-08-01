package gift.exception.kakao;

import org.springframework.http.HttpStatus;

public class TokenReauthenticationRequiredException extends KakaoException {
    public TokenReauthenticationRequiredException(String message) {

        super(message, HttpStatus.UNAUTHORIZED);
    }
}
