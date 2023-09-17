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


    @PostMapping("/answers/{memberId}")
    public ResponseEntity<?> writeAnswers(@PathVariable("memberId") Long memberId,
                                          @RequestBody List<ReqAnswersToQues> request) {
        memberService.writeAnswerToQuestions(request, memberId);

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

    @GetMapping("/answers/{memberId}")
    public ResponseEntity<?> getAllAnswersByMemberId(@PathVariable("memberId") Long memberId){
        List<ResAllAnswers> allAnswersByMemberId = memberService.getAllAnswersByMemberId(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(allAnswersByMemberId);
    }


}
