package gift.e2e;

import gift.dto.member.MemberRequestDto;
import gift.dto.member.TokenResponseDto;
import gift.dto.wish.WishRequestDto;
import gift.dto.wish.WishResponseDto;
import gift.service.member.MemberService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class WishControllerTest {

    @Autowired
    private MemberService memberService;

    @LocalServerPort
    private int port;

    private RestClient client;
    private String baseUrl;
    private String memberToken1;
    private String memberToken2;

    @BeforeAll
    void setUp() {
        client = RestClient.builder().build();
        baseUrl = "http://localhost:" + port;

        var loginRequest1 = new MemberRequestDto("user@example.com", "0000");
        var loginRequest2 = new MemberRequestDto("user2@example.com", "0000");

        TokenResponseDto tokenResponseDto1 = client.post()
                .uri(baseUrl + "/api/members/login")
                .body(loginRequest1)
                .retrieve()
                .body(TokenResponseDto.class);

        TokenResponseDto tokenResponseDto2 = client.post()
                .uri(baseUrl + "/api/members/login")
                .body(loginRequest2)
                .retrieve()
                .body(TokenResponseDto.class);

        this.memberToken1 = tokenResponseDto1.getToken();
        this.memberToken2 = tokenResponseDto2.getToken();
    }

    @DisplayName("토큰을 받아 검증 후 Member 객체를 생성하여 해당 Member의 위시 리스트에 상품을 추가한다.")
    @Test
    void 정상적인_위시리스트_상품_추가() {

        WishRequestDto wishRequestDto = new WishRequestDto(2L);

        var response = client.post()
                .uri(baseUrl + "/api/wishes")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + memberToken1)
                .body(wishRequestDto)
                .retrieve()
                .toEntity(WishResponseDto.class);

        WishResponseDto dtoBody = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        assert dtoBody != null;
        assertThat(dtoBody.getName()).isEqualTo("카카오프랜즈 인형");
    }

    @DisplayName("토큰을 받아 검증 후 Member 객체를 생성하여 해당 Member에 대한 전체 위시를 찾는다.")
    @Test
    void 정상적인_위시_전체조회() {

        var response = client.get()
                .uri(baseUrl + "/api/wishes/all")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + memberToken1)
                .retrieve()
                .toEntity(WishResponseDto[].class);
        WishResponseDto[] wishList = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @DisplayName("토큰을 받아 검증 후 Member 객체를 생성하여 해당 Member의 위시 리스트에 상품을 삭제한다..")
    @Test
    void 정상적인_위시리스트_상품_삭제() {
        var result = client.delete()
                .uri(baseUrl + "/api/wishes/1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + memberToken1)
                .retrieve()
                .toBodilessEntity();

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @DisplayName("중복되는 상품을 위시리스트에 추가할 시, 409 에러코드와 메세지가 나타나는지 테스트")
    @Test
    void 중복되는_상품을_위시리스트에_추가() {
        WishRequestDto wishRequestDto = new WishRequestDto(2L);

        assertThatExceptionOfType(HttpClientErrorException.Conflict.class)
                .isThrownBy(
                        () ->
                                client.post()
                                        .uri(baseUrl + "/api/wishes")
                                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + memberToken2)
                                        .body(wishRequestDto)
                                        .retrieve()
                                        .toEntity(Void.class)
                )
                .satisfies(ex -> {
                    String body = ex.getResponseBodyAsString();
                    assertThat(body).contains("해당 상품은 이미 위시 리스트에 존재합니다.");
                });
    }

    @DisplayName("토큰 없이 상품을 위시리스트에 추가할 시, 409 에러코드와 메세지가 나타나는지 테스트")
    @Test
    void 토큰_없이_상품을_위시리스트에_추가() {
        WishRequestDto wishRequestDto = new WishRequestDto(2L);

        assertThatExceptionOfType(HttpClientErrorException.Unauthorized.class)
                .isThrownBy(
                        () ->
                                client.post()
                                        .uri(baseUrl + "/api/wishes")
                                        .body(wishRequestDto)
                                        .retrieve()
                                        .toEntity(Void.class)
                )
                .satisfies(ex -> {
                    String body = ex.getResponseBodyAsString();
                    assertThat(body).contains("토큰이 존재하지 않거나 유효하지 않습니다.");
                });
    }

    @DisplayName("다른 사람의 위시리스트에 잘못 접근하여 상품을 삭제하는 경우, 403에러코드와 메세지 출력")
    @Test
    void 자신의_위시리스트가_아닌_상품을_삭제() {
        assertThatExceptionOfType(HttpClientErrorException.Forbidden.class)
                .isThrownBy(
                        () ->
                                client.delete()
                                        .uri(baseUrl + "/api/wishes/2")
                                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + memberToken1)
                                        .retrieve()
                                        .toEntity(Void.class)
                )
                .satisfies(ex -> {
                    String body = ex.getResponseBodyAsString();
                    assertThat(body).contains("삭제할 권한이 없습니다.");
                });
    }


}
