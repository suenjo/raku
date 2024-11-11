package rakuproject.raku.domain.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rakuproject.raku.domain.auth.dto.request.AuthenticationRequest;
import rakuproject.raku.domain.auth.dto.response.JsonWebTokenResponse;
import rakuproject.raku.domain.auth.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping
    public ResponseEntity<JsonWebTokenResponse> auth(
            @RequestBody AuthenticationRequest request
    ){
        return ResponseEntity.ok(authService.auth(request));
    }

//    @PostMapping //auth
//    public void auth( @RequestBody AuthenticationRequest request)
//    {
//        authService.auth(request);
//    }


}
