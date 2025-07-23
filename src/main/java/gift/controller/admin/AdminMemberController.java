package gift.controller.admin;

import gift.dto.member.MemberRequestDto;
import gift.dto.member.MemberResponseDto;
import gift.dto.member.MemberRoleRequestDto;
import gift.service.member.MemberService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/members")
public class AdminMemberController {
    private final MemberService memberService;

    public AdminMemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 멤버 추가 페이지로 이동
    @GetMapping("/new")
    public String goNewMemberForm() {
        return "admin/newMemberForm";
    }

    // 멤버 수정 페이지로 이동
    @GetMapping("/{id}/edit")
    public String editMemberForm(@PathVariable Long id, Model model) {

        MemberResponseDto member = memberService.findMemberByIdElseThrow(id);
        model.addAttribute("member", member);

        return "admin/editMemberForm";
    }

    // 상품 목록 조회 기능
    @GetMapping
    public String findMembers(
            @RequestParam(value = "id", required = false) Long id,
            Model model)
    {
        if (id == null) {
            List<MemberResponseDto> dtoList = memberService.findAllMembers();
            model.addAttribute("members", dtoList);

            return "admin/members";
        }

        Optional<MemberResponseDto> dto = memberService.findMemberById(id);

        if (dto.isPresent())
            model.addAttribute("members", List.of(dto));
        else
            model.addAttribute("members", Collections.emptyList());

        return "admin/members";
    }

    // 상품 추가 기능 구현
    @PostMapping
    public String saveMembers(@Valid @ModelAttribute MemberRequestDto dto) {

        memberService.registerMember(dto);
        return "redirect:/admin/members";
    }

    // 상품 수정 기능 구현
    @PutMapping("{id}")
    public String updateMember(
            @PathVariable Long id,
            @Valid @ModelAttribute MemberRoleRequestDto dto) {

        memberService.updateMember(id, dto);
        return "redirect:/admin/members";
    }

    // 상품 삭제  기능 구현
    @DeleteMapping("{id}")
    public String deleteMember(@PathVariable Long id) {

        memberService.deleteMember(id);
        return "redirect:/admin/members";
    }
}