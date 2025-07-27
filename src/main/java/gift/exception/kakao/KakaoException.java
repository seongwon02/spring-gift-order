package gift.exception.kakao;

import org.springframework.http.HttpStatus;

public class KakaoException extends RuntimeException {
    private final HttpStatus httpStatus;

    public KakaoException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
