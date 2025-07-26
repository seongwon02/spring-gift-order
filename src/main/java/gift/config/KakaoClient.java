package gift.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.kakao.KakaoTokenResponseDto;
import gift.exception.kakao.KakaoException;
import gift.exception.kakao.KakaoInvalidValueException;
import gift.exception.kakao.KakaoOAuthExcetion;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Component
public class KakaoClient {

    private final RestClient restClient;
    private final String clientId;
    private final String redirectUri;

    public KakaoClient(
            ObjectMapper objectMapper,
            @Value("${kakao.rest.api.key}") String clientId,
            @Value("${kakao.redirect.uri}") String redirectUri
    ) {
        this.clientId = clientId;
        this.redirectUri = redirectUri;

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofSeconds(5));
        requestFactory.setReadTimeout(Duration.ofSeconds(5));

        this.restClient = RestClient.builder()
                .baseUrl("https://kauth.kakao.com")
                .requestFactory(requestFactory)
                .defaultStatusHandler(HttpStatusCode::is4xxClientError, (request, response) -> {
                    HttpStatus status = HttpStatus.valueOf(response.getStatusCode().value());
                    JsonNode errorNode = objectMapper.readTree(response.getBody());
                    String errorCode = errorNode.path("error_code").asText();

                    switch (status.value()) {
                        case 400:
                            throw new KakaoInvalidValueException("잘못된 요청입니다: " + errorCode);
                        case 401:
                            throw new KakaoOAuthExcetion("인증에 실패했습니다: " + errorCode);
                        default:
                            throw new KakaoException("처리되지 않은 클라이언트 오류입니다: " + errorCode, status);
                    }
                })
                .build();
    }

    public KakaoTokenResponseDto getToken(String code) {

        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        return restClient.post()
                .uri("/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .body(KakaoTokenResponseDto.class);
    }
}
