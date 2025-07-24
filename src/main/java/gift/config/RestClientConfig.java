package gift.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.exception.kakao.KakaoOAuthExcetion;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient kakaoOAuthRestClient() {
        return RestClient.builder()
                .baseUrl("https://kauth.kakao.com")
                .defaultStatusHandler(HttpStatusCode::isError, (request, response) -> {

                    String errorBody = new String(response.getBody().readAllBytes());
                    HttpStatus httpStatus = HttpStatus.valueOf(response.getStatusCode().value());

                    throw new KakaoOAuthExcetion(errorBody, httpStatus);
                })
                .build();
    }
}
