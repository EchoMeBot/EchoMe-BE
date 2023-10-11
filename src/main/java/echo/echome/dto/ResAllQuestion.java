package echo.echome.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResAllQuestion {

    private Long quesId;
    private String content;
    private Long quesNum;
}
