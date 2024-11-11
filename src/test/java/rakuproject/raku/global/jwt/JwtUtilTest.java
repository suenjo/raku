package rakuproject.raku.global.jwt;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    void testJwtDecryption()
    {
        String accessToken = jwtUtil.generateAccessToken("example12");
        Claims claims = jwtUtil.getClaims(accessToken);
        System.out.println("===================================");
        System.out.println(claims.getSubject());
        System.out.println(claims.getExpiration());

    }

    @Test
    void testGeneratedAccessToken()
    {
        String accessToken = jwtUtil.generateAccessToken("example12");
        System.out.println(accessToken);
        System.out.println("======================================================");

    }

}