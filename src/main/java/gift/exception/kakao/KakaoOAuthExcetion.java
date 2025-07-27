package gift.exception.kakao;

import org.springframework.http.HttpStatus;

public class KakaoOAuthExcetion extends KakaoException {


    public KakaoOAuthExcetion(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }

}
