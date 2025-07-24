package gift.exception.kakao;

import org.springframework.http.HttpStatus;

public class KakaoOAuthExcetion extends RuntimeException {

    private HttpStatus httpStatus;

    public KakaoOAuthExcetion(String message, HttpStatus httpStatus) {
        super(String.format("%d %s: %s",
                httpStatus.value(),
                httpStatus.getReasonPhrase(),
                message
        ));
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
