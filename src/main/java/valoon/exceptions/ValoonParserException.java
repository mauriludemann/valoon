package valoon.exceptions;

public class ValoonParserException extends BaseException {

    public ValoonParserException(String message) {
        this("valoon_parser_exception", message, (Throwable)null);
    }

    public ValoonParserException(String errorCode, String message, Throwable cause) {
        super(500, errorCode, message, cause);
    }
}
