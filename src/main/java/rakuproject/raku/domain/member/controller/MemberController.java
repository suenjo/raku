package rakuproject.raku.domain.member.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rakuproject.raku.domain.member.dto.MemberDTO;
import rakuproject.raku.domain.member.service.MemberService;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    //회원 가입 엔드포인트
    @PostMapping("/membership")
    public ResponseEntity<String> registerMember(@RequestBody MemberDTO memberDTO) {
        Long memberId = memberService.registerMember(memberDTO);
        return ResponseEntity.ok("Member registered with ID: " + memberId);
    }


}
