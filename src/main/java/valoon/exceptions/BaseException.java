package valoon.exceptions;

public class BaseException extends RuntimeException {

    private final Integer statusCode;
    private final String errorCode;

    public BaseException(Integer statusCode, String errorCode, String message) {
        this(statusCode, errorCode, message, (Throwable) null);
    }

    public BaseException(Integer statusCode, String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
        this.errorCode = errorCode;
    }

    public Integer getStatusCode() {
        return this.statusCode;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public String toString() {
        return String.format("%s: %s", this.getClass(), this.getMessage());
    }

    public ExceptionMessage toExceptionMessage() {
        return new ExceptionMessage(this);
    }
}
