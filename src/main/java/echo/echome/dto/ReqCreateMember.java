package echo.echome.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReqCreateMember {

    @Email(message = "Should write email")
    @NotBlank(message = "Email should not be blank")
    private String email;

    @NotBlank(message = "Name should not be blank")
    @Size(min = 2,message = "Name should be at least 2 character")
    private String name;

    @NotBlank(message = "Password should not be blank")
    @Size(min = 8,message = "Password should be at least 8 character")
    private String pwd;

    @NotBlank(message = "PhoneNum should not be blank")
    @Size(min = 3,message = "PhoneNum should be at least 3 character")
    private String phoneNum;

}
