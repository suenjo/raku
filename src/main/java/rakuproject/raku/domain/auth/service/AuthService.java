package rakuproject.raku.domain.auth.service;

import rakuproject.raku.domain.auth.dto.request.AuthenticationRequest;
import rakuproject.raku.domain.auth.dto.response.JsonWebTokenResponse;

public interface AuthService {

    JsonWebTokenResponse auth(AuthenticationRequest request);
    JsonWebTokenResponse refresh( String token);

}
