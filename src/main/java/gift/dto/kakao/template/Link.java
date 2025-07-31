package gift.dto.kakao.template;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Link(
        @JsonProperty("web_url")
        String webLink,

        @JsonProperty("mobile_web_url")
        String mobileWebUrl
) {
}
