package echo.echome.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReqAnswersToQues {

    private Long quesId;//질문 객체 id
    private String content;//답변 내용

}
