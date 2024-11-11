package rakuproject.raku.global.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import rakuproject.raku.domain.member.dto.MemberDTO;
import rakuproject.raku.domain.member.exception.NotFoundMemberException;
import rakuproject.raku.domain.member.mapper.MemberMapper;
import rakuproject.raku.domain.member.repository.MemberRepository;
import rakuproject.raku.global.jwt.properties.JwtProperties;
import rakuproject.raku.global.security.CustomUserDetails;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    private final JwtProperties jwtProperties;
    private final MemberRepository memberRepository;
    //access token

    public String generateAccessToken(String id){
        return Jwts.builder()
                .setSubject(id)
                .setIssuedAt(
                        new Date(System.currentTimeMillis())
                )// 발행
                .setExpiration(
                        new Date(System.currentTimeMillis()+jwtProperties.getExpiration())
                )//만료
                .signWith(SignatureAlgorithm.HS256,jwtProperties.getSecretkey())
                .compact();

    }
    public String generateRefreshToken(String id){
        return Jwts.builder()
                .setSubject(id)
                .setIssuedAt(
                        new Date(System.currentTimeMillis())
                )// 발행
                .setExpiration(
                        new Date(System.currentTimeMillis()+jwtProperties.getRefreshExpriation())
                )//만료
                .signWith(SignatureAlgorithm.HS256,jwtProperties.getSecretkey())
                .compact();
    }
    //토큰 복호화 및 클레임 확인
    public Claims getClaims(String token)
    {
        try{
            return Jwts.parserBuilder()
                    .setSigningKey(jwtProperties.getSecretkey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e){
            throw new JwtException("Expired JWT");
        } catch (UnsupportedJwtException e){
            throw new JwtException("Unsupporeted JWT");
        }catch (MalformedJwtException e){
            throw new JwtException("Invalid JWT"); //서명예외
        }catch (SignatureException e){
            throw new JwtException("Unsupporeted JWT");
        } catch (IllegalArgumentException e){
            throw new JwtException("JWT claims string is empty");
        }
    }

    //사용자 인증
    public Authentication getAuthentication(String token)
    {
        Claims claims = getClaims(token);
        String id=claims.getSubject();

        MemberDTO memberDTO= memberRepository.
                findById(id).
                map(MemberMapper::createDTO
                ).orElseThrow(()-> NotFoundMemberException.EXCEPTION
                );


        //사용자 인증 객체
        CustomUserDetails customUserDetails = CustomUserDetails.create(memberDTO);

        return new UsernamePasswordAuthenticationToken
                (customUserDetails, null, customUserDetails.getAuthorities());
    }
//我添加的
    public boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

}