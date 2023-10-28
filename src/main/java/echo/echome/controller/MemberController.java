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
    public ResponseEntity<ResCreateMember> createNewMember(@RequestBody @Valid ReqCreateMember request) {
        ResCreateMember response = memberService.createNewMember(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid ReqMemberLogin reqMemberLogin,
        HttpServletResponse httpServletResponse){

        Token token = memberService.login(reqMemberLogin.getEmail(),reqMemberLogin.getPassword());
        httpServletResponse.addHeader("accessToken",token.getAccessToken());
        ResMemberInfo memberInfo = memberService.getMemberInfo(token.getAccessToken());
        Cookie cookie = new Cookie("refreshToken", token.getRefreshToken());
        cookie.setPath("/");
        cookie.setMaxAge(3000000);
        cookie.isHttpOnly();
        cookie.setSecure(true);

        httpServletResponse.addCookie(cookie);
        return ResponseEntity.ok().body(memberInfo);
    }

    @GetMapping("/info")
    public ResponseEntity<?> getMemberInfo(@RequestHeader("Auhorization") String authorizationHeader){
        String accessToken = authorizationHeader.replace("Bearer ", "");
        ResMemberInfo memberInfo = memberService.getMemberInfo(accessToken);
        return ResponseEntity.ok(memberInfo);
    }

    @PatchMapping("/update")
    public ResponseEntity<?> updateMemberInfo(@RequestHeader("Authorization") String authorizationHeader,
                                              @RequestBody ReqUpdateMember request){
        String accessToken = authorizationHeader.replace("Bearer ", "");
        memberService.updateMember(accessToken,request);
        return ResponseEntity.ok("회원 정보 업데이트 완료.");
    }

    @GetMapping("/chat")
    public ResponseEntity<String> getAllAnswersOfMember(@RequestBody ReqMemberChat request) {

        String context = memberService.makeContext(request);
        return ResponseEntity.ok().body("{\"context\": \"" + context + "\"}");
    }

    @GetMapping("/answers")
    public ResponseEntity<?> getAllAnswersByMemberId(@RequestHeader("Authorization") String authorizationHeader){

        String accessToken = authorizationHeader.replace("Bearer ","");
        List<ResAllAnswers> allAnswersByMemberId = memberService.getAllAnswersByMemberId(accessToken);
        return ResponseEntity.status(HttpStatus.OK).body(allAnswersByMemberId);
    }

}
