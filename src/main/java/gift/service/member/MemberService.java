package gift.service.member;

import gift.dto.member.MemberRequestDto;
import gift.dto.member.MemberResponseDto;
import gift.dto.member.MemberRoleRequestDto;
import gift.dto.member.TokenResponseDto;

import java.util.List;
import java.util.Optional;

public interface MemberService {

    TokenResponseDto registerMember(MemberRequestDto dto);
    TokenResponseDto logInMember(MemberRequestDto dto);

    List<MemberResponseDto> findAllMembers();
    Optional<MemberResponseDto> findMemberById(Long id);
    MemberResponseDto findMemberByIdElseThrow(Long id);
    MemberResponseDto saveMember(MemberRequestDto dto);
    MemberResponseDto updateMember(Long id, MemberRoleRequestDto dto);
    void deleteMember(Long id);
}
