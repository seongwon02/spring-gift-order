package gift.dto.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoUserInfoResponseDto (
        @JsonProperty("id")
        Long id,

        @JsonProperty("kakao_account")
        KakaoAccount kakaoAccount
){
    public record KakaoAccount(
            String email
    ){

    }
}
