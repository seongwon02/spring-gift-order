package gift.exception.kakao;

import org.springframework.http.HttpStatus;

public class KakaoOAuthException extends KakaoException {


    public KakaoOAuthException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }

}
