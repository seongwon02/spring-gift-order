package gift.controller.api;

import gift.dto.kakao.KakaoTokenResponseDto;
import gift.dto.member.TokenResponseDto;
import gift.service.kakao.KakaoOAuthService;
import gift.service.member.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/oauth")
public class KaKaoOAuthController {

    private final KakaoOAuthService kakaoLoginService;
    private final MemberService memberService;

    public KaKaoOAuthController(KakaoOAuthService kakaoLoginService, MemberService memberService) {
        this.kakaoLoginService = kakaoLoginService;
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity<TokenResponseDto> login(@RequestParam("code") String code) {
        KakaoTokenResponseDto kakaoToken = kakaoLoginService.getInitialAccessToken(code);

        TokenResponseDto tokenDto = memberService.loginWithKakao(kakaoToken);
        return ResponseEntity.ok(tokenDto);
    }
}
