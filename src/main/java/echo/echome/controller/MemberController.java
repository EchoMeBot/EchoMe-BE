package echo.echome.controller;

import echo.echome.dto.*;
import echo.echome.service.MemberService;
import echo.echome.utils.Token;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/members")
@CrossOrigin
public class MemberController {

    private final MemberService memberService;
    private final Environment env;

    @GetMapping("/health_check")
    public String check() {
        return "It's starting on Port " + env.getProperty("local.server.port").toString();
    }


    @PostMapping("/create")
    public ResponseEntity<ResCreateMember> createNewMem(@RequestBody @Valid ReqCreateMember request) {
        ResCreateMember response = memberService.createNewMember(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid ReqMemberLogin reqMemberLogin,
        HttpServletResponse httpServletResponse){

        Token token = memberService.login(reqMemberLogin.getEmail(),reqMemberLogin.getPassword());
        httpServletResponse.addHeader("accessToken",token.getAccessToken());

        Cookie cookie = new Cookie("refreshToken", token.getRefreshToken());
        cookie.setPath("/");
        cookie.setMaxAge(3000000);
        cookie.isHttpOnly();
        cookie.setSecure(true);

        httpServletResponse.addCookie(cookie);
        return ResponseEntity.ok().body("로그인에 성공했습니다.");
    }

    @GetMapping("/info")
    public ResponseEntity<?> getMemberInfo(@RequestHeader("Auhorization") String authorizationHeader){
        String accessToken = authorizationHeader.replace("Bearer ", "");
        ResMemberInfo memberInfo = memberService.getMemberInfo(accessToken);
        return ResponseEntity.ok(memberInfo);
    }

    //모든 질문에 한번에 대답하는 코드
    @PostMapping("/answers")
    public ResponseEntity<?> writeAnswers(@RequestHeader("Authorization") String authorizationHeader,
                                          @RequestBody List<ReqAnswersToQues> request) {
        String accessToken = authorizationHeader.replace("Bearer ","");
        memberService.writeAnswerToQuestions(request, accessToken);

        return ResponseEntity.status(HttpStatus.OK).body("답변이 정상적으로 등록되었습니다.");
    }

    @PostMapping("/answer")
    public ResponseEntity<?> answerOneQuestion(@RequestHeader("Authorization") String authorizationToken,
                                               @RequestBody ReqEachAnswer request ){
        String accessToken = authorizationToken.replace("Bearer ","");
        memberService.answerToOneQuestion(accessToken, request);
        log.info("request: {}",request.getAnswer());
        return ResponseEntity.status(HttpStatus.OK).body("답변이 정상적으로 등록되었습니다.");
    }

//    @GetMapping("/answers/{memberId}")
//    public ResponseEntity<String> getAllAnswersOfMember(@PathVariable("memberId") Long memberId) {
//        List<ResAllAnswers> returnValue = memberService.getAllAnswers(memberId);
//
//        String context = memberService.makeContext(returnValue);
//
//        //return ResponseEntity.status(HttpStatus.OK).body(context);
//        return ResponseEntity.ok().body("{\"context\": \"" + context + "\"}");
//    }

    @GetMapping("/answers")
    public ResponseEntity<?> getAllAnswersByMemberId(@RequestHeader("Authorization") String authorizationHeader){

        String accessToken = authorizationHeader.replace("Bearer ","");
        List<ResAllAnswers> allAnswersByMemberId = memberService.getAllAnswersByMemberId(accessToken);
        return ResponseEntity.status(HttpStatus.OK).body(allAnswersByMemberId);
    }


//    @GetMapping("/chat/{memberId}")
//    public ResponseEntity<?> chatWithEcho(@PathVariable("memberId") Long memberId){
//        memberService.getAllAnswersByMemberId()
//        return ResponseEntity.status(HttpStatus.OK).body()
//    }


}
