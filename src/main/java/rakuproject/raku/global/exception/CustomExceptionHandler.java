package rakuproject.raku.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import rakuproject.raku.global.exception.reponse.ErrorResponse;

@RestControllerAdvice // 예외를 전역적으로 처리
public class CustomExceptionHandler {
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity handlerCustomException(
            CustomException e
    ) {
        ResponseEntity<ErrorResponse> response = ResponseEntity
                .status(e.getHttpStatus())
                .body(
                        new ErrorResponse(e.getHttpStatus(), e.getMessage())
                );
        return response;
    }
    @ExceptionHandler(Exception.class)
    protected ResponseEntity handlerException(Exception e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
