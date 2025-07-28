package gift.service.kakao;

import gift.config.KakaoClient;
import gift.dto.kakao.KakaoTokenRefreshResponseDto;
import gift.dto.kakao.KakaoTokenResponseDto;
import gift.entity.Member;
import gift.entity.MemberKakaoToken;
import gift.exception.kakao.KakaoTokenNotFoundException;
import gift.exception.kakao.TokenReauthenticationRequiredException;
import gift.repository.MemberKakaoTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
public class KakaoOAuthService {

    private final KakaoClient kakaoClient;
    private final MemberKakaoTokenRepository memberKakaoTokenRepository;

    public KakaoOAuthService(KakaoClient kakaoClient, MemberKakaoTokenRepository memberKakaoTokenRepository){
        this.kakaoClient = kakaoClient;
        this.memberKakaoTokenRepository = memberKakaoTokenRepository;
    }

    public KakaoTokenResponseDto getAccessToken(String code) {

        return kakaoClient.getToken(code);
    }

    @Transactional
    public String getValidAccessToken(Member member) {
        MemberKakaoToken memberKakaoToken = memberKakaoTokenRepository.findById(member.getId())
                .orElseThrow(() -> new KakaoTokenNotFoundException("해당 사용자의 카카오 토큰이 존재하지 않습니다."));

        if (!memberKakaoToken.isAccessTokenExpired())
            return memberKakaoToken.getAccessToken();

        if (memberKakaoToken.isRefreshTokenExpired())
                throw new TokenReauthenticationRequiredException("모든 토큰이 만료되었습니다. 로그인이 필요합니다.");

        KakaoTokenRefreshResponseDto response = kakaoClient.reissueToken(memberKakaoToken);

        Instant now = Instant.now();
        Instant newAccessTokenExpiresAt = now.plusSeconds(response.expiresIn());

        String newRefreshToken = Optional.ofNullable(response.refreshToken())
                .orElse(memberKakaoToken.getRefreshToken());

        Instant newRefreshTokenExpiresAt = Optional.ofNullable(response.refreshTokenExpiresIn())
                        .map(now::plusSeconds)
                        .orElse(memberKakaoToken.getRefreshTokenExpiresAt());

        memberKakaoToken.updateToken(
                response.accessToken(),
                newRefreshToken,
                newAccessTokenExpiresAt,
                newRefreshTokenExpiresAt
                );

        return response.accessToken();
    }
}
