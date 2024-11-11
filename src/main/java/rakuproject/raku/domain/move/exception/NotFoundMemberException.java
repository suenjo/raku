package rakuproject.raku.domain.move.exception;

public class NotFoundMemberException extends RuntimeException {
    public NotFoundMemberException(String message) {
        super(message);
    }

    public NotFoundMemberException() {
        super();
    }
}

