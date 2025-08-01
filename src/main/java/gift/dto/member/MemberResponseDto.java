package gift.dto.member;

import gift.entity.RoleType;

public class MemberResponseDto {

    private Long id;
    private String email;
    private String password;
    private RoleType role;

    public MemberResponseDto(Long id, String email, String password, RoleType role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public RoleType getRole() {
        return role;
    }
}
