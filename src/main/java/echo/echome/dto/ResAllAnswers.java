package echo.echome.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResAllAnswers {

    private Long quesId; //질문 PK
    private String question;//질문 내용
    private String answer;//해당 질문에 회원이 한 답변
}
