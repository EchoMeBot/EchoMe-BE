package echo.echome.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResMemberInfo {

    private Long memberId;
    private String email;
    private String name;
    private String phoneNum;
    private UUID unique;
}
