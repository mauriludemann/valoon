package valoon.exceptions;

public class JacksonJsonMappingException extends BaseException {

    public JacksonJsonMappingException(String message) {
        this("json_mapping_exception", message, (Throwable)null);
    }

    public JacksonJsonMappingException(String errorCode, String message, Throwable cause) {
        super(500, errorCode, message, cause);
    }
}
