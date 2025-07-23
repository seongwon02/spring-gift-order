package gift.dto.member;

import gift.entity.RoleType;
import jakarta.validation.constraints.NotNull;

public class MemberRoleRequestDto {

    @NotNull(message = "역할은 필수 입력입니다.")
    private final RoleType role;

    public MemberRoleRequestDto(RoleType role) {
        this.role = role;
    }

    public RoleType getRole() {
        return role;
    }
}
