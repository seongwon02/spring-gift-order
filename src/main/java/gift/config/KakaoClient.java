package gift.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.kakao.KakaoTokenRefreshResponseDto;
import gift.dto.kakao.KakaoTokenResponseDto;
import gift.dto.kakao.KakaoUserInfoResponseDto;
import gift.dto.kakao.template.Link;
import gift.dto.kakao.template.MessageTemplate;
import gift.entity.MemberKakaoToken;
import gift.entity.Option;
import gift.entity.Order;
import gift.entity.Product;
import gift.exception.kakao.KakaoException;
import gift.exception.kakao.KakaoInvalidValueException;
import gift.exception.kakao.KakaoOAuthException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.util.Optional;

@Component
public class KakaoClient {

    private final RestClient oAuthClient;
    private final RestClient apiClient;
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

        this.oAuthClient = RestClient.builder()
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
                            throw new KakaoOAuthException("인증에 실패했습니다: " + errorCode);
                        default:
                            throw new KakaoException("처리되지 않은 클라이언트 오류입니다: " + errorCode, status);
                    }
                })
                .build();

        this.apiClient = RestClient.builder()
                .baseUrl("https://kapi.kakao.com")
                .requestFactory(requestFactory)
                .build();
    }

    public KakaoTokenResponseDto getToken(String code) {

        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        return oAuthClient.post()
                .uri("/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .body(KakaoTokenResponseDto.class);
    }

    public KakaoTokenRefreshResponseDto reissueToken(MemberKakaoToken memberKakaoToken) {
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "refresh_token");
        body.add("client_id", clientId);
        body.add("refresh_token", memberKakaoToken.getRefreshToken());

        return oAuthClient.post()
                .uri("/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .body(KakaoTokenRefreshResponseDto.class);
    }

    public KakaoUserInfoResponseDto getUserInfo(String accessToken) {
        return apiClient.get()
                .uri("/v2/user/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .body(KakaoUserInfoResponseDto.class);
    }

    public void sendOrderToMe(String accessToken, Order order) {

        ObjectMapper objectMapper = new ObjectMapper();
        var body = new LinkedMultiValueMap<String, String>();

        MessageTemplate template = getOrderTemplate(order);
        String templateJson;

        try {
            templateJson = objectMapper.writeValueAsString(template);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("카카오 메세지 JSON 변환 실패");
        }

        body.add("template_object", templateJson);

        apiClient.post()
                .uri("/v2/api/talk/memo/default/send")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .toBodilessEntity();
    }

    private MessageTemplate getOrderTemplate (Order order) {
        Option option = order.getOption();
        Product product = option.getProduct();

        String orderInfo = String.format(
                "[주문 정보]\n상품: %s\n옵션: %s\n수량: %d\n메세지: %s",
                product.getName(),
                option.getName(),
                order.getQuantity(),
                Optional.ofNullable(order.getMessage()).orElse("")
        );

        Link link = new Link("http://localhost:8080", "http://localhost:8080");

        return new MessageTemplate(orderInfo, link, "주문 확인");
    }

}
