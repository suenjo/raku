package rakuproject.raku.global.exception.reponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Setter
@Getter
public class ErrorResponse {
    private HttpStatus httpStatus;
    private String message;
}
