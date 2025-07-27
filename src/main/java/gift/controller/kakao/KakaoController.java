package gift.controller.kakao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
public class KakaoController {

    @Value("${kakao.rest.api.key}")
    private String kakaoRestApiKey;

    @Value("${kakao.redirect.uri}")
    private String kakaoRedirectUri;

    @GetMapping("/login")
    public String loginPage(Model model) {
        String kakaoLoginUrl = UriComponentsBuilder
                .fromUriString("https://kauth.kakao.com/oauth/authorize")
                .queryParam("client_id", kakaoRestApiKey)
                .queryParam("redirect_uri", kakaoRedirectUri)
                .queryParam("response_type", "code")
                .queryParam("scope", "talk_message account_email")
                .toUriString();

        model.addAttribute("kakaoLoginUrl", kakaoLoginUrl);

        return "kakao/login";
    }
}
