package gift.exception.kakao;

import org.springframework.http.HttpStatus;

public class KakaoTokenNotFoundException extends KakaoException {
    public KakaoTokenNotFoundException(String message) {

        super(message, HttpStatus.NOT_FOUND);
    }
}
