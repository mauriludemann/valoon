package valoon.exceptions;

public class BadRequestException extends BaseException {

    public BadRequestException(String message) {
        this("bad_request", message, (Throwable)null);
    }

    public BadRequestException(String errorCode, String message, Throwable cause) {
        super(400, errorCode, message, cause);
    }
}
