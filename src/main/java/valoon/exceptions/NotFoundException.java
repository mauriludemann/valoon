package valoon.exceptions;

public class NotFoundException extends BaseException {

    public NotFoundException(String message) {
        this("not_found", message, (Throwable)null);
    }

    public NotFoundException(String errorCode, String message, Throwable cause) {
        super(404, errorCode, message, cause);
    }
}
