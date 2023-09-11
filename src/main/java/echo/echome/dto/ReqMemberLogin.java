package echo.echome.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ReqMemberLogin {

    @NotBlank(message = "이메일 형식으로 입력해주세요.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    @Builder
    public ReqMemberLogin(String email,String password){
        this.email= email;
        this.password=password;
    }


}
