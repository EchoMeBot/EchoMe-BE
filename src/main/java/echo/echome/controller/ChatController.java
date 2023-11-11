package echo.echome.controller;

import echo.echome.dto.ResMakeContext;
import echo.echome.service.MemberService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/chats")
@RequiredArgsConstructor
public class ChatController {

    private final MemberService memberService;

    @GetMapping("/{uuid}")
    public ResponseEntity<?> chatFromRandomUser(@PathVariable("uuid") UUID memberUUID){

        ResMakeContext context = memberService.makeContextByUUID(memberUUID);
        return ResponseEntity.ok().body(context);
    }
}
