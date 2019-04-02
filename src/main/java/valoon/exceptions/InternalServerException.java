package valoon.exceptions;

public class InternalServerException extends BaseException {

    public InternalServerException(String message) {
        this("internal_server_exception", message, (Throwable)null);
    }

    public InternalServerException(String errorCode, String message, Throwable cause) {
        super(500, errorCode, message, cause);
    }
}
