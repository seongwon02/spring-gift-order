package gift.e2e;

import gift.dto.product.ProductRequestDto;
import gift.dto.product.ProductResponseDto;
import gift.dto.product.option.OptionRequestDto;
import gift.dto.product.option.OptionResponseDto;
import gift.service.product.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerTest {

    @Autowired
    private ProductService productService;

    @LocalServerPort
    private int port;

    private RestClient client;
    private String url;

    @BeforeEach
    void setUp() {
        client = RestClient.builder().build();
        url = "http://localhost:" + port + "/api/products";
    }

    @DisplayName("상품 생성 시, 정상적으로 생성되고 응답이 올바른지 테스트")
    @Test
    void 정상적인_상품저장() {

        ProductRequestDto requestDto = new ProductRequestDto(
                "인형",
                39900L,
                "http://example.png"
        );

        var response = client.post()
                .uri(url)
                .body(requestDto)
                .retrieve()
                .toEntity(ProductResponseDto.class);

        ProductResponseDto dtoBody = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        assert dtoBody != null;
        assertThat(dtoBody.getName()).isEqualTo("인형");
        assertThat(dtoBody.getPrice()).isEqualTo(39900L);
        assertThat(dtoBody.getImageUrl()).isEqualTo("http://example.png");
        assertThat(dtoBody.getApproved()).isEqualTo(true);
    }

    @DisplayName("상품 전체 조회 시, 정상적으로 모든 상품을 가져오는지 테스트")
    @Test
    void 정상적인_전체상품_조회() {

        var response = client.get()
                .uri(url + "/all")
                .retrieve()
                .toEntity(ProductResponseDto[].class);

        ProductResponseDto[] allProducts = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


    @DisplayName("이름이 15자를 초과하는 상품 생성 시, 400 에러코드와 메세지 반환하는지 테스트")
    @Test
    void 상품길이가_15자를_초과하는_경우의_상품저장() {

        ProductRequestDto requestDto = new ProductRequestDto(
                "이것은이름이겁나게길고긴상품이름",
                9900L,
                "http://example.png"
        );

        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
                .isThrownBy(
                    () ->
                        client.post()
                            .uri(url)
                            .body(requestDto)
                            .retrieve()
                            .toEntity(Void.class)
                )
                .satisfies(ex -> {
                    String body = ex.getResponseBodyAsString();
                    assertThat(body).contains("상품명은 최대 15자를 가질 수 있습니다.");
                });
    }

    @DisplayName("허용하지 않는 특수문자가 포함된 상품 생성 시, 400 에러코드와 메세지 반환하는지 테스트")
    @Test
    void 허용하지_않는_특수문자가_포함된_상품저장() {

        ProductRequestDto requestDto = new ProductRequestDto(
                "@@상품이름@@",
                9900L,
                "http://example.png"
        );

        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
                .isThrownBy(
                        () ->
                                client.post()
                                        .uri(url)
                                        .body(requestDto)
                                        .retrieve()
                                        .toEntity(Void.class)
                )
                .satisfies(ex -> {
                    String body = ex.getResponseBodyAsString();
                    assertThat(body).contains("상품명에 허용하지 않는 특수문자가 포함되어 있습니다. 허용하는 특수문자: (), [], +, -, &, /, _");
                });
    }

    @DisplayName("상품가격이 음수인 상품 생성 시, 400 에러코드와 메세지 반환하는지 테스트")
    @Test
    void 상품가격이_음수인_상품저장() {

        ProductRequestDto requestDto = new ProductRequestDto(
                "인형",
                -10000L,
                "http://example.png"
        );

        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
                .isThrownBy(
                        () ->
                                client.post()
                                        .uri(url)
                                        .body(requestDto)
                                        .retrieve()
                                        .toEntity(Void.class)
                )
                .satisfies(ex -> {
                    String body = ex.getResponseBodyAsString();
                    assertThat(body).contains("가격은 0원 이상입니다.");
                });
    }

    @DisplayName("잘못된 이미지url을 가진 상품 생성 시, 400 에러코드와 메세지 반환하는지 테스트")
    @Test
    void 잘못된_이미지URL을_가진_상품저장() {

        ProductRequestDto requestDto = new ProductRequestDto(
                "인형",
                10000L,
                "example.png"
        );

        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
                .isThrownBy(
                        () ->
                                client.post()
                                        .uri(url)
                                        .body(requestDto)
                                        .retrieve()
                                        .toEntity(Void.class)
                )
                .satisfies(ex -> {
                    String body = ex.getResponseBodyAsString();
                    assertThat(body).contains("유효한 이미지 URL 형식이 아닙니다.");
                });
    }

    @DisplayName("카카오 문구가 포함된 상품 생성 시, 201코드와 함께 approved가 false를 가지는지 테스트")
    @Test
    void 카카오문구가_포함된_상품저장() {

        ProductRequestDto requestDto = new ProductRequestDto(
                "카카오 인형",
                39900L,
                "http://example.png"
        );

        var response = client.post()
                .uri(url)
                .body(requestDto)
                .retrieve()
                .toEntity(ProductResponseDto.class);

        ProductResponseDto dtoBody = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        assert dtoBody != null;
        assertThat(dtoBody.getApproved()).isEqualTo(false);
        assertThat(dtoBody.getDescription()).isEqualTo("카카오 문구가 담긴 상품은 담당 MD와 협의 후 사용가능합니다.");
    }

    @DisplayName("카카오 문구가 포함된 상품 수정 시, 200코드와 함께 approved가 false를 가지는지 테스트")
    @Test
    void 카카오문구가_포함된_상품수정() {

        ProductRequestDto requestDto = new ProductRequestDto(
                "카카오 인형",
                39900L,
                "http://example.png"
        );

        var response = client.put()
                .uri(url + "/1")
                .body(requestDto)
                .retrieve()
                .toEntity(ProductResponseDto.class);

        ProductResponseDto dtoBody = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        assert dtoBody != null;
        assertThat(dtoBody.getApproved()).isEqualTo(false);
        assertThat(dtoBody.getDescription()).isEqualTo("카카오 문구가 담긴 상품은 담당 MD와 협의 후 사용가능합니다.");
    }

    @DisplayName("정상적으로 상품 옵션 추가가 되는지 테스트")
    @Test
    void 정상적인_상품_옵션_생성(){

        OptionRequestDto requestDto = new OptionRequestDto(
                "L size",
                100L
        );

        var response = client.post()
                .uri(url + "/2/options")
                .body(requestDto)
                .retrieve()
                .toEntity(OptionResponseDto.class);

        OptionResponseDto responseDto = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        assertThat(responseDto.getName()).isEqualTo("L size");
        assertThat(responseDto.getQuantity()).isEqualTo(100L);
    }

    @DisplayName("정상적으로 상품 옵션 조회가 되는지 테스트")
    @Test
    void 정상적인_상품_옵션_조회(){

        var response = client.get()
                .uri(url + "/2/options")
                .retrieve()
                .toEntity(OptionResponseDto[].class);

        OptionResponseDto[] responseDto = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

    @DisplayName("정상적으로 상품 옵션 수정이 되는지 테스트")
    @Test
    void 정상적인_상품_옵션_수정(){

        OptionRequestDto requestDto = new OptionRequestDto(
                "XL size",
                100L
        );

        var response = client.put()
                .uri(url + "/2/options/2")
                .body(requestDto)
                .retrieve()
                .toEntity(OptionResponseDto.class);

        OptionResponseDto responseDto = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseDto.getName()).isEqualTo("XL size");
        assertThat(responseDto.getQuantity()).isEqualTo(100L);

    }

    @DisplayName("정상적으로 상품 옵션 삭제가 되는지 테스트")
    @Test
    void 정상적인_상품_옵션_삭제(){

        var response = client.delete()
                .uri(url + "/2/options/1")
                .retrieve()
                .toBodilessEntity();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @DisplayName("중복된 옵션을 추가할 때의 에러코드와 메세지 테스트")
    @Test
    void 중복된_상품_옵션_생성(){

        OptionRequestDto requestDto = new OptionRequestDto(
                "M size",
                100L
        );

        assertThatExceptionOfType(HttpClientErrorException.Conflict.class)
                .isThrownBy(
                        () ->
                                client.post()
                                        .uri(url + "/2/options")
                                        .body(requestDto)
                                        .retrieve()
                                        .toEntity(OptionResponseDto.class)
                )
                .satisfies(ex -> {
                    String body = ex.getResponseBodyAsString();
                    assertThat(body).contains("해당 상품에 동일한 옵션이 존재합니다.");
                });
    }
}
