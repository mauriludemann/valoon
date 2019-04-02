package valoon.exceptions;

public class InvalidOrderStatusException extends BaseException {

    public InvalidOrderStatusException(String message) {
        this("invalid_order_status", message, (Throwable)null);
    }

    public InvalidOrderStatusException(String errorCode, String message, Throwable cause) {
        super(400, errorCode, message, cause);
    }
}
