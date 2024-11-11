package rakuproject.raku.domain.member.exception;

import org.springframework.http.HttpStatus;
import rakuproject.raku.global.exception.CustomException;

public class NotFoundMemberException extends CustomException {
    public static final CustomException EXCEPTION =
            new NotFoundMemberException("Company not found");
    public NotFoundMemberException(String companyNotFound) {
        super(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다.");
    }
}
