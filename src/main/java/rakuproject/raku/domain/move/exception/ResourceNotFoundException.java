package rakuproject.raku.domain.move.exception;
//异常处理（Exception）
public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
