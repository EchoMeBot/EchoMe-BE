package echo.echome.controller;

import echo.echome.dto.ReqEachAnswer;
import echo.echome.service.AnswerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/answers")
//@CrossOrigin
public class AnswerController {

    private final AnswerService answerService;


    @PatchMapping("/update")
    public ResponseEntity<?> updateAnswer(@RequestBody ReqEachAnswer request){
        log.info("updateAnswer Controller");
        answerService.updateAnswer(request);
        return ResponseEntity.ok().body("답변 수정 완료");
    }


}
