package gift.exception.kakao;

import org.springframework.http.HttpStatus;

public class KakaoInvalidValueException extends KakaoException{

    public KakaoInvalidValueException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
