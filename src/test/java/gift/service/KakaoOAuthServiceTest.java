package gift.service;

import gift.config.KakaoClient;
import gift.dto.kakao.KakaoTokenResponseDto;
import gift.exception.kakao.KakaoInvalidValueException;
import gift.service.kakao.KakaoOAuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class KakaoOAuthServiceTest {

    @InjectMocks
    private KakaoOAuthService kakaoOAuthService;

    @Mock
    private KakaoClient kakaoClient;

    @DisplayName("정상적으로 카카오 로그인하고 토큰을 반환하는지 테스트")
    @Test
    void 카카오로그인_이후_정상적인_토큰반환() {
        // given
        String validCode = "valid-auth-code";
        KakaoTokenResponseDto expectedDto = new KakaoTokenResponseDto(
                "bearer",
                "test-access-token",
                0,
                null,
                0,
                null
        );

        when(kakaoClient.getToken(validCode)).thenReturn(expectedDto);

        // when
        KakaoTokenResponseDto actualDto = kakaoOAuthService.getAccessToken(validCode);

        // then
        assertThat(actualDto.accessToken()).isEqualTo("test-access-token");
        assertThat(actualDto.tokenType()).isEqualTo("bearer");
    }

    @DisplayName("유효하지 않은 인가코드로 인한 카카오 로그인 실패")
    @Test
    void 잘못된_인가코드로_카카오로그인_실패() {
        // given
        String invalidCode = "invalid-auth-code";
        String errorMessage = "인가 코드가 유효하지 않습니다.";

        when(kakaoClient.getToken(invalidCode)).thenThrow(new KakaoInvalidValueException(errorMessage));

        // when, then
        KakaoInvalidValueException exception = assertThrows(
                KakaoInvalidValueException.class,
                () -> kakaoOAuthService.getAccessToken(invalidCode)
        );

        assertThat(exception.getMessage()).isEqualTo(errorMessage);
    }
}
