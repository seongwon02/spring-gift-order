package gift.dto.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KakaoTokenResponseDto {

    @JsonProperty("token_type") // JSON의 "token_type" 필드를 tokenType 필드에 매핑
    private String tokenType;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private Integer expiresIn;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("refresh_token_expires_in")
    private Integer refreshTokenExpiresIn;

    @JsonProperty("scope")
    private String scope;

    public KakaoTokenResponseDto(String tokenType,
                                 String accessToken,
                                 Integer expiresIn,
                                 String refreshToken,
                                 Integer refreshTokenExpiresIn,
                                 String scope) {
        this.tokenType = tokenType;
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.refreshToken = refreshToken;
        this.refreshTokenExpiresIn = refreshTokenExpiresIn;
        this.scope = scope;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Integer getRefreshTokenExpiresIn() {
        return refreshTokenExpiresIn;
    }

    public String getScope() {
        return scope;
    }
}
