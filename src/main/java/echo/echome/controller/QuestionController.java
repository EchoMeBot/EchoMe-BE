package echo.echome.controller;

import echo.echome.dto.ReqNewQuestion;
import echo.echome.dto.ResAllQuestion;
import echo.echome.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/questions")
@CrossOrigin
public class QuestionController {

    private final QuestionService questionService;


    @GetMapping("")
    public List<ResAllQuestion> getAllQuestions() {
        //return ResponseEntity.status(HttpStatus.OK).body(context);
        return questionService.getAllQuestions();
    }


    @PostMapping("/add")
    public ResponseEntity<?> addNewQuestion(@RequestBody ReqNewQuestion question){
        Long returnValue = questionService.addQuestion(question.getQuesNumber(), question.getQuestionContent());
        return ResponseEntity.status(HttpStatus.CREATED).body(returnValue);
    }
}

