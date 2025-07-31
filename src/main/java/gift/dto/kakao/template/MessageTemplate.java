package gift.dto.kakao.template;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MessageTemplate(
        @JsonProperty("object_type")
        String objectType,

        String text,

        Link link,

        @JsonProperty("button_title")
        String buttonTitle
) {
        public MessageTemplate(String text, Link link, String buttonTitle) {
                this("text", text, link, buttonTitle);
        }

}
