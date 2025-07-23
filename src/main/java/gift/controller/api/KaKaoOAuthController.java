package gift.controller.api;

import gift.dto.kakao.KakaoTokenResponseDto;
import gift.service.kakao.KakaoOAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/oauth")
public class KaKaoOAuthController {

    private final KakaoOAuthService kakaoLoginService;

    public KaKaoOAuthController(KakaoOAuthService kakaoLoginService) {
        this.kakaoLoginService = kakaoLoginService;
    }

    @GetMapping
    public ResponseEntity<KakaoTokenResponseDto> login(@RequestParam("code") String code) {
        KakaoTokenResponseDto accessToken = kakaoLoginService.getAccessToken(code);
        return ResponseEntity.ok(accessToken);
    }
}
