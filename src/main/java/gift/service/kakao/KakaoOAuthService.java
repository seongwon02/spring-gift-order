package gift.service.kakao;

import gift.config.KakaoClient;
import gift.dto.kakao.KakaoTokenResponseDto;
import org.springframework.stereotype.Service;

@Service
public class KakaoOAuthService {

    private final KakaoClient kakaoClient;

    public KakaoOAuthService(KakaoClient kakaoClient){
        this.kakaoClient = kakaoClient;
    }

    public KakaoTokenResponseDto getAccessToken(String code) {

        return kakaoClient.getToken(code);
    }
}
