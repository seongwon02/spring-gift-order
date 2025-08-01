package gift.controller.api;

import gift.dto.member.MemberRequestDto;
import gift.dto.member.MemberResponseDto;
import gift.dto.member.MemberRoleRequestDto;
import gift.dto.member.TokenResponseDto;
import gift.service.member.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<TokenResponseDto> register (
            @Valid @RequestBody MemberRequestDto dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(memberService.registerMember(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> logIn (
            @Valid @RequestBody MemberRequestDto dto) {

        return ResponseEntity.ok(memberService.loginMember(dto));
    }

    @GetMapping
    public ResponseEntity<List<MemberResponseDto>> findAllMembers() {
        List<MemberResponseDto> memberList = memberService.findAllMembers();

        return ResponseEntity.ok(memberList);
    }

    @GetMapping("{id}")
    public ResponseEntity<MemberResponseDto> findMember(@PathVariable Long id) {

        MemberResponseDto dto = memberService.findMemberByIdElseThrow(id);

        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<MemberResponseDto> createMember(
            @Valid @RequestBody MemberRequestDto dto) {

        return ResponseEntity.status(HttpStatus.CREATED).
                body(memberService.saveMember(dto));
    }

    @PutMapping("{id}")
    public ResponseEntity<MemberResponseDto> updateMember(
            @Valid @RequestBody MemberRoleRequestDto dto,
            @PathVariable Long id) {

        return ResponseEntity.ok(memberService.updateMember(id, dto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);

        return ResponseEntity.noContent().build();
    }
}
