package echo.echome.dto;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqNewQuestion implements Serializable {
    private Long quesNumber;
    private String questionContent;
}
