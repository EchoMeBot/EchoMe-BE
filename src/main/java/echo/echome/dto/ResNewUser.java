package echo.echome.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResNewUser {
    @NotNull(message = "should be write email")
    @Size(min = 2,message = "email should be more than 2 character")
    @Email
    private String email;
    @NotNull(message = "should be write pwd")
    @Size(min = 8,message = "pwd should be more than 8 character")
    private String pwd;

}
