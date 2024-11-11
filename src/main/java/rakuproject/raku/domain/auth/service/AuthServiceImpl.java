package rakuproject.raku.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import rakuproject.raku.domain.auth.dto.request.AuthenticationRequest;
import rakuproject.raku.domain.auth.dto.response.JsonWebTokenResponse;
import rakuproject.raku.domain.member.dto.MemberDTO;
import rakuproject.raku.global.jwt.JwtUtil;
import rakuproject.raku.global.security.CustomUserDetails;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;


    @Override
    public JsonWebTokenResponse auth(AuthenticationRequest request) {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getId(), request.getPwd()
                        )
                );

        MemberDTO memberDTO = ((CustomUserDetails) authentication.getPrincipal()).getMemberDTO();

//        System.out.println(memberDTO.getEmail());
        return JsonWebTokenResponse.builder().accessToken(
                        jwtUtil.generateAccessToken(memberDTO.getId()
                        ))
                .refreshToken(jwtUtil.generateRefreshToken(memberDTO.getId()))
                .build();
    }

    @Override
    public JsonWebTokenResponse refresh(String token) {
        return null;
    }
}
