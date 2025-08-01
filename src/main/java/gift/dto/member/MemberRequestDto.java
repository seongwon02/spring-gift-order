package gift.dto.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class MemberRequestDto {

    @NotBlank(message = "이메일을 입력해야 합니다.")
    @Email(message = "이메일 형식과 맞지 않습니다.")
    @Size(min = 5, max = 100, message = "이메일은 5~100자 입니다.")
    private final String email;

    @NotBlank(message = "비밀번호를 입력해야 합니다.")
    @Size(min = 4, max = 16, message = "비밀번호는 4~16자 입니다.")
    private final String password;

    public MemberRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }


    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
