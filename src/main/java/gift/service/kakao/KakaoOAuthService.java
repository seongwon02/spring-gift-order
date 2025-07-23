package gift.service.kakao;

import gift.dto.kakao.KakaoTokenResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class KakaoOAuthService {

    private final RestClient restClient;
    private final String clientId;
    private final String redirectUri;

    public KakaoOAuthService(
            RestClient restClient,
            @Value("${kakao.rest.api.key}") String clientId,
            @Value("${kakao.redirect.uri}") String redirectUri) {
        this.restClient = restClient;
        this.clientId = clientId;
        this.redirectUri = redirectUri;

        System.out.println(clientId);
    }

    public KakaoTokenResponseDto getAccessToken(String code) {

        String url = "https://kauth.kakao.com/oauth/token";

        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        KakaoTokenResponseDto response = restClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .body(KakaoTokenResponseDto.class);

        return response;
    }
}
