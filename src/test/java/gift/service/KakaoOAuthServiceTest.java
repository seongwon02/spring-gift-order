package gift.service;

import gift.config.KakaoClient;
import gift.dto.kakao.KakaoTokenRefreshResponseDto;
import gift.dto.kakao.KakaoTokenResponseDto;
import gift.entity.Member;
import gift.entity.MemberKakaoToken;
import gift.entity.RoleType;
import gift.exception.kakao.KakaoInvalidValueException;
import gift.exception.kakao.TokenReauthenticationRequiredException;
import gift.repository.MemberKakaoTokenRepository;
import gift.service.kakao.KakaoOAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class KakaoOAuthServiceTest {

    @InjectMocks
    private KakaoOAuthService kakaoOAuthService;

    @Mock
    private KakaoClient kakaoClient;

    @Mock
    private MemberKakaoTokenRepository memberKakaoTokenRepository;

    private Member member;

    @BeforeEach
    void setUp() {
        member = new Member("test@example.com", "password", RoleType.USER);
    }

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
        KakaoTokenResponseDto actualDto = kakaoOAuthService.getInitialAccessToken(validCode);

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
                () -> kakaoOAuthService.getInitialAccessToken(invalidCode)
        );

        assertThat(exception.getMessage()).isEqualTo(errorMessage);
    }

    @DisplayName("액세스 토큰이 유효하면 기존 토큰을 반환하는지 테스트")
    @Test
    void 정상적인_토큰_반환() {
        // given
        String validAccessToken = "valid-access-token";
        Instant tokenExpiresAt = Instant.now().plusSeconds(3600);
        MemberKakaoToken kakaoToken = new MemberKakaoToken(member, validAccessToken, "refresh-token", tokenExpiresAt, tokenExpiresAt);

        given(memberKakaoTokenRepository.findByMemberId(member.getId())).willReturn(Optional.of(kakaoToken));

        // when
        String resultToken = kakaoOAuthService.getValidAccessToken(member);

        // then
        assertThat(resultToken).isEqualTo(validAccessToken);
    }

    @DisplayName("엑세스 토큰 만료 시 리프레쉬 토큰으로 재발급을 하는지 테스트")
    @Test
    void 토큰_재발급() {
        // given
        Instant expiredAt = Instant.now().minusSeconds(1);
        Instant tokenExpiresAt = Instant.now().plusSeconds(3600);
        MemberKakaoToken expiredToken = new MemberKakaoToken(member, "expired-access-token", "valid-refresh-token", expiredAt, tokenExpiresAt);

        String newAccessToken = "new-access-token";
        KakaoTokenRefreshResponseDto refreshResponse = new KakaoTokenRefreshResponseDto(
                "bearer", newAccessToken, 21599, null, null
        );

        given(memberKakaoTokenRepository.findByMemberId(member.getId())).willReturn(Optional.of(expiredToken));
        given(kakaoClient.reissueToken(expiredToken)).willReturn(refreshResponse);

        // when
        String resultToken = kakaoOAuthService.getValidAccessToken(member);

        // then
        assertThat(resultToken).isEqualTo(newAccessToken);
        assertThat(expiredToken.getAccessToken()).isEqualTo(newAccessToken);
    }

    @DisplayName("리프레쉬 토큰까지 만료 시 예외가 발생하는지 테스트")
    @Test
    void 모든_토큰이_만료된_경우() {
        // given
        String errorMessage = "모든 토큰이 만료되었습니다. 로그인이 필요합니다.";
        Instant expiredAt = Instant.now().minusSeconds(1); // 모두 만료된 시간
        MemberKakaoToken allExpiredToken = new MemberKakaoToken(member, "expired-access-token", "expired-refresh-token", expiredAt, expiredAt);

        given(memberKakaoTokenRepository.findByMemberId(member.getId())).willReturn(Optional.of(allExpiredToken));

        // when & then
        TokenReauthenticationRequiredException exception = assertThrows(
                TokenReauthenticationRequiredException.class,
                () -> kakaoOAuthService.getValidAccessToken(member)
        );

        assertThat(exception.getMessage()).isEqualTo(errorMessage);
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.UNAUTHORIZED);

    }


}
