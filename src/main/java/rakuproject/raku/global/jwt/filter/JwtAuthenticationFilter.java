package rakuproject.raku.global.jwt.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import rakuproject.raku.global.jwt.JwtUtil;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter { //jwt를 인증할 필터

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal
            (HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String jwt=null;
        String authorization=request.getHeader(HttpHeaders.AUTHORIZATION);

        if(StringUtils.hasText(authorization) && authorization.startsWith("Bearer")){
            //Bearer JWT
            jwt= authorization.substring(7);
        }
        if(jwt != null){

//            Authentication authentication=jwtUtil.getAuthentication(jwt);
            //Authentication 객체가 저장되는 보관소
            SecurityContextHolder.getContext().setAuthentication(
                    jwtUtil.getAuthentication(jwt)
            );
        }
        //전처리
        filterChain.doFilter(request,response);
        //후처리

    }
}
