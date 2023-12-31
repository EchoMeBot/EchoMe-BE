package echo.echome.controller;

import echo.echome.dto.ReqAnswersToQues;
import echo.echome.dto.ReqCreateMember;
import echo.echome.dto.ResAllAnswers;
import echo.echome.dto.ResCreateMember;
import echo.echome.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/members")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080"}, allowCredentials = "true")
public class MemberController {

    private final MemberService memberService;
    private final Environment env;

    @GetMapping("/health_check")
    public String check() {
        return "It's starting on Port " + env.getProperty("local.server.port").toString();
    }


    @PostMapping("/create")
    public ResponseEntity<ResCreateMember> createNewMem(@RequestBody ReqCreateMember request) {
        ResCreateMember response = memberService.createNewMember(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PostMapping("/answers/{memberId}")
    public ResponseEntity<?> writeAnswers(@PathVariable("memberId") Long memberId,
                                          @RequestBody List<ReqAnswersToQues> request) {
        memberService.writeAnswerToQuestions(request, memberId);

        return ResponseEntity.status(HttpStatus.OK).body(memberId);
    }

    @GetMapping("/answers/{memberId}")
    public ResponseEntity<String> getAllAnswersOfMember(@PathVariable("memberId") Long memberId) {
        List<ResAllAnswers> returnValue = memberService.getAllAnswers(memberId);

        String context = memberService.makeContext(returnValue);

        //return ResponseEntity.status(HttpStatus.OK).body(context);
        return ResponseEntity.ok().body("{\"context\": \"" + context + "\"}");
    }
}
