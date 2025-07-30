package gift.entity;

import gift.dto.kakao.KakaoTokenRefreshResponseDto;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.Optional;

@Entity
@Table(name = "member_kakao_token")
public class MemberKakaoToken {

    @Id
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private Member member;

    @Column(nullable = false, name = "access_token")
    private String accessToken;

    @Column(nullable = false, name = "refresh_token")
    private String refreshToken;

    @Column(nullable = false, name = "access_token_expires_at")
    private Instant accessTokenExpiresAt;

    @Column(nullable = false, name = "refresh_token_expires_at")
    private Instant refreshTokenExpiresAt;

    protected MemberKakaoToken() {}

    public MemberKakaoToken(Member member,
                            String accessToken,
                            String refreshToken,
                            Instant accessTokenExpiresAt,
                            Instant refreshTokenExpiresAt
    ) {
        this.member = member;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpiresAt = accessTokenExpiresAt;
        this.refreshTokenExpiresAt = refreshTokenExpiresAt;
    }

    public void updateToken(KakaoTokenRefreshResponseDto response){
        Instant now = Instant.now();

        this.accessToken = response.accessToken();
        this.accessTokenExpiresAt = now.plusSeconds(response.expiresIn());

        if (Optional.ofNullable(response.refreshToken()).isPresent() &&
            Optional.ofNullable(response.refreshTokenExpiresIn()).isPresent()) {
            this.refreshToken = response.refreshToken();
            this.accessTokenExpiresAt = now.plusSeconds(response.refreshTokenExpiresIn());
        }
    }

    public boolean isAccessTokenExpired() {
        return accessTokenExpiresAt.isBefore(Instant.now());
    }

    public boolean isRefreshTokenExpired() {
        return refreshTokenExpiresAt.isBefore(Instant.now());
    }

    public Member getMember() {
        return member;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Instant getAccessTokenExpiresAt() {
        return accessTokenExpiresAt;
    }

    public Instant getRefreshTokenExpiresAt() {
        return refreshTokenExpiresAt;
    }
}
