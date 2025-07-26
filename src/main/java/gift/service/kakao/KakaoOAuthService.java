package gift.service.kakao;

import gift.config.KakaoClient;
import gift.dto.kakao.KakaoTokenResponseDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

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
