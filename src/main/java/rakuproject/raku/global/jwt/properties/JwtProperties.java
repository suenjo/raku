package rakuproject.raku.global.jwt.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "application.jwt")
@Getter
@Setter
public class JwtProperties {
    private String secretkey;
    private long expiration;
    private long refreshExpriation;
}
